package me.ele.talaris.napos.getorder.task;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import me.ele.talaris.constant.DeliveryOrderContant;
import me.ele.talaris.deliveryorder.dto.DeliveryOrder;
import me.ele.talaris.deliveryorder.persistent.dao.DeliveryOrderDao;
import me.ele.talaris.errorlog.processor.ErrorLogProcessor;
import me.ele.talaris.log.holder.LogHolder;
import me.ele.talaris.model.Context;
import me.ele.talaris.napos.model.TEleOrder;
import me.ele.talaris.redis.CacheLock;
import me.ele.talaris.redis.LockedObject;
import me.ele.talaris.utils.SerializeUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
public class TransformOrder {
    public static final Logger logger = LoggerFactory.getLogger(TransformOrder.class);
    @Autowired
    DeliveryOrderDao deliveryOrderDao;
    @Autowired
    ErrorLogProcessor errorLogProcessor;

    /**
     * 将饿单转化为配送单，该方法同时会将转化后的配送单写入DB。若已经转化过了，则不做DB操作
     * 
     * @param context
     * @param tEleOrders
     * @param actionType 扫码/勾选确认创建的
     * @param status
     * @return
     */
    public void changeEleOrdersToDeliveryOrders(Context context, List<TEleOrder> tEleOrders, String actionType,
            int status) {
        if (CollectionUtils.isEmpty(tEleOrders)) {
            return;
        }
        List<Long> eleOrderIds = new ArrayList<Long>();
        for (TEleOrder eleOrder : tEleOrders) {
            eleOrderIds.add(eleOrder.getId());
        }
        List<DeliveryOrder> deliveryOrders = deliveryOrderDao.getDeliveryOrdersByEleIdList(eleOrderIds);
        // 全部不存在，直接做写入操作
        if (CollectionUtils.isEmpty(deliveryOrders)) {
            List<DeliveryOrder> newDeliveryOrders = transformEleOrderToDeliveryOrder(context, actionType, status,
                    tEleOrders);
            deliveryOrderDao.batchDeliveryOrderInsert(newDeliveryOrders);

        } else {
            List<Long> deliveryOrderIds = new ArrayList<Long>();
            for (DeliveryOrder deliveryOrder : deliveryOrders) {
                deliveryOrderIds.add(deliveryOrder.getEle_order_id());
            }
            // 比较出来那些已经在库里面
            List<DeliveryOrder> newDeliveryOrders = new ArrayList<DeliveryOrder>();
            Iterator<TEleOrder> iterator = tEleOrders.iterator();
            while (iterator.hasNext()) {
                TEleOrder tEleOrder = iterator.next();
                boolean isExist = false;
                for (DeliveryOrder deliveryOrder : deliveryOrders) {
                    if (tEleOrder.getId() == deliveryOrder.getEle_order_id()) {
                        isExist = true;
                        break;
                    }
                }
                if (isExist) {
                    iterator.remove();
                }
            }
            if (CollectionUtils.isEmpty(tEleOrders)) {
                return;
            }
            List<Long> atLasteleOrderIds = new ArrayList<Long>();
            for (TEleOrder eleOrder : tEleOrders) {
                atLasteleOrderIds.add(eleOrder.getId());
            }
            logger.info("拉取过来的单号有：" + changerListToString(eleOrderIds) + "存在的单号有："
                    + changerListToString(deliveryOrderIds) + "最后存在的单号有:" + changerListToString(atLasteleOrderIds));
            newDeliveryOrders = transformEleOrderToDeliveryOrder(context, actionType, status, tEleOrders);
            deliveryOrderDao.batchDeliveryOrderInsert(newDeliveryOrders);

        }
        LogHolder.bunchLog("成功拉取;");
    }

    private List<DeliveryOrder> transformEleOrderToDeliveryOrder(Context context, String actionType, int status,
            List<TEleOrder> eleOrders) {
        List<DeliveryOrder> deliveryOrders = new ArrayList<DeliveryOrder>();
        for (TEleOrder eleOrder : eleOrders) {
            DeliveryOrder deliveryOrder = new DeliveryOrder();
            deliveryOrder.setCreated_at(new Timestamp(System.currentTimeMillis()));
            deliveryOrder.setCreated_type(Integer.valueOf(actionType));
            deliveryOrder.setEle_order_id(Long.valueOf(eleOrder.getId()));
            deliveryOrder.setEle_order_sn(eleOrder.getGeneral().getDaySn());
            deliveryOrder.setEle_created_time(new Timestamp(eleOrder.getGeneral().getCreatedTime().getTime() * 1000));
            if (eleOrder.getGeneral().getBookedTime() != null) {
                deliveryOrder.setBooked_time(new Timestamp(eleOrder.getGeneral().getBookedTime().getTime() * 1000));
            }
            deliveryOrder.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
            if (eleOrder.getGeneral().isOnlinePaid()) {
                deliveryOrder.setPaied_amount(eleOrder.getDetail().getTotal());
            } else {
                deliveryOrder.setPaied_amount(new BigDecimal(0));
            }
            deliveryOrder.setPayment_type(eleOrder.getGeneral().isOnlinePaid() ? DeliveryOrderContant.ONLINE_PAID
                    : DeliveryOrderContant.CASH_ON_DELIVERY);
            deliveryOrder.setReceiver_address(eleOrder.getConsignee().getAddress());
            deliveryOrder.setReceiver_mobile(getMobile(eleOrder));
            deliveryOrder.setReceiver_name(StringUtils.isEmpty(eleOrder.getConsignee().getName()) ? "" : eleOrder
                    .getConsignee().getName());
            deliveryOrder.setRst_id(eleOrder.getRestaurantId());
            // 目前source是只有来自饿了么的订单
            deliveryOrder.setSource(DeliveryOrderContant.SOURCE_FROM_ELEME);
            deliveryOrder.setStation_id(context.getUserStationRoles().get(0).getStation_id());
            deliveryOrder.setStatus(status);
            deliveryOrder.setTaker_id(context.getUser().getId());
            deliveryOrder.setTaker_mobile(context.getUser().getMobile());
            deliveryOrder.setTotal_amount(eleOrder.getDetail().getTotal());
            deliveryOrder.setUpdated_at(new Timestamp(System.currentTimeMillis()));
            deliveryOrder.setEle_order_detail(SerializeUtil.beanToJson(eleOrder));
            deliveryOrders.add(deliveryOrder);
        }
        return deliveryOrders;
    }

    @CacheLock(lockedPrefix = "CHANGE_ELE_ORDERTO_DELIVERYORDER", timeOut = 3000, expireTime = 4)
    public void process(Context context, List<TEleOrder> tEleOrders, @LockedObject int rstID) {
        if (CollectionUtils.isEmpty(tEleOrders)) {
            LogHolder.bunchLog(rstID + "餐厅拉取了0单子;");
            return;
        }
        try {
            LogHolder.bunchLog(rstID + "餐厅拉取了" + tEleOrders.size() + "单子;");
            logger.info("{}餐厅拉取订单{}单", rstID, tEleOrders.size());
            changeEleOrdersToDeliveryOrders(context, tEleOrders,
                    DeliveryOrderContant.FETCH_ORDER_ACTION_TYPE_BY_SYSTEM, DeliveryOrderContant.WAITTO_DELIVERY);
        } catch (Throwable e) {
            LogHolder.bunchLog("转化配送单失败" + rstID + ";");
            logger.error("失败", e);
            errorLogProcessor.writeErrorInfo(e);
            errorLogProcessor.writeErrorInfo("锁失败");
        }
    }

    private long getMobile(TEleOrder tEleOrder) {
        // 直接做手机位十一位的校验
        List<String> phones = tEleOrder.getConsignee().getPhones();

        for (String phone : phones) {
            if (phone.length() == 11) {
                try {
                    long longPhone = Long.valueOf(phone);
                    return longPhone;
                } catch (Exception e) {
                    continue;
                }
            }

        }
        errorLogProcessor.writeErrorInfo("获取号码失败");
        return 0l;
    }

    private String changerListToString(List<Long> ids) {
        StringBuilder stringBuilder = new StringBuilder();
        int count = ids.size();
        for (int i = 0; i < count; i++) {
            if (i != count - 1) {
                stringBuilder.append(ids.get(i)).append(",");
            } else {
                stringBuilder.append(ids.get(i));
            }
        }
        return stringBuilder.toString();
    }

    //
    // for (TEleOrder eleOrder : tEleOrders) {
    // DeliveryOrder deliveryOrder = null;
    // // 这里为了判断饿单是否已经转化为了配送单
    // deliveryOrder = deliveryOrderDao.getDeliveryOrderByEleOrderId(eleOrder.getId());
    // if (deliveryOrder != null) {
    // if (deliveryOrder.getStatus() == DeliveryOrderContant.WAITTO_DELIVERY) {
    // deliveryOrder.setCreated_type(Integer.valueOf(actionType));
    // deliveryOrder.setCreated_at(new Timestamp(System.currentTimeMillis()));
    // deliveryOrder.setUpdated_at(new Timestamp(System.currentTimeMillis()));
    // // 这里有为什么还要去更新时间：若代配送的单子是在两个小时内的，然后配送员被结账，如果不更新时间，则配送员可能看不到这几个单子了
    // deliveryOrderDao.update(deliveryOrder);
    // }
    // continue;
    // }
    // deliveryOrder = new DeliveryOrder();
    // deliveryOrder.setCreated_at(new Timestamp(System.currentTimeMillis()));
    // deliveryOrder.setCreated_type(Integer.valueOf(actionType));
    // deliveryOrder.setEle_order_id(Long.valueOf(eleOrder.getId()));
    // deliveryOrder.setEle_order_sn(eleOrder.getGeneral().getDaySn());
    // deliveryOrder.setEle_created_time(new Timestamp(eleOrder.getGeneral().getCreatedTime().getTime() * 1000));
    // if (eleOrder.getGeneral().getBookedTime() != null) {
    // deliveryOrder.setBooked_time(new Timestamp(eleOrder.getGeneral().getBookedTime().getTime() * 1000));
    // }
    // deliveryOrder.setUuID(UUID.randomUUID().toString().replaceAll("-", ""));
    // if (eleOrder.getGeneral().isOnlinePaid()) {
    // deliveryOrder.setPaied_amount(eleOrder.getDetail().getTotal());
    // } else {
    // deliveryOrder.setPaied_amount(new BigDecimal(0));
    // }
    // deliveryOrder.setPayment_type(eleOrder.getGeneral().isOnlinePaid() ? DeliveryOrderContant.ONLINE_PAID
    // : DeliveryOrderContant.CASH_ON_DELIVERY);
    // deliveryOrder.setReceiver_address(eleOrder.getConsignee().getAddress());
    // deliveryOrder.setReceiver_mobile(getMobile(eleOrder));
    // deliveryOrder.setReceiver_name(StringUtils.isEmpty(eleOrder.getConsignee().getName()) ? "" : eleOrder
    // .getConsignee().getName());
    // deliveryOrder.setRst_id(eleOrder.getRestaurantId());
    // // 目前source是只有来自饿了么的订单
    // deliveryOrder.setSource(DeliveryOrderContant.SOURCE_FROM_ELEME);
    // deliveryOrder.setStation_id(context.getUserStationRoles().get(0).getStation_id());
    // deliveryOrder.setStatus(status);
    // deliveryOrder.setTaker_id(context.getUser().getId());
    // deliveryOrder.setTaker_mobile(context.getUser().getMobile());
    // deliveryOrder.setTotal_amount(eleOrder.getDetail().getTotal());
    // deliveryOrder.setUpdated_at(new Timestamp(System.currentTimeMillis()));
    // deliveryOrder.setEle_order_detail(SerializeUtil.beanToJson(eleOrder));
    // deliveryOrderDao.addDeliveryOrder(deliveryOrder);
    // }
}
