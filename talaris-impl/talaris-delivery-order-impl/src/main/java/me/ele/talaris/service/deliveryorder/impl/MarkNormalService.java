package me.ele.talaris.service.deliveryorder.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.ele.talaris.constant.DeliveryOrderContant;
import me.ele.talaris.deliveryorder.dto.DeliveryOrder;
import me.ele.talaris.deliveryorder.persistent.dao.DeliveryOrderDao;
import me.ele.talaris.deliveryorder.persistent.dao.DeliveryOrderRecordDao;
import me.ele.talaris.dao.EleOrderDetailDao;
import me.ele.talaris.exception.SystemException;
import me.ele.talaris.exception.UserException;
import me.ele.talaris.model.Context;
import me.ele.talaris.deliveryorder.persistent.eb.DeliveryOrderRecord;
import me.ele.talaris.service.deliveryorder.IMarkNormalService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class MarkNormalService implements IMarkNormalService {
    private final static Logger logger = LoggerFactory.getLogger(MarkNormalService.class);
    @Autowired
    DeliveryOrderDao deliveryOrderDao;
    @Autowired
    DeliveryOrderRecordDao deliveryOrderRecordDao;
    @Autowired
    EleOrderDetailDao eleOrderDetailDao;
    @Autowired
    DeliveryOrderComponent deliveryOrderComponent;
    @Autowired
    CommonDeliveryOrderService commonDeliveryOrderService;
    private static final String EXCEPTIONMSG = "系统自动标记异常--订单超过4小时未送达";

    /**
     * 标记为正常，返回的是正常的配送单信息的map，key为配送单ID
     * 
     * @param context
     * @param deliveryOrderIds
     * @return
     * @throws UserException
     */
    @Override
    public Map<Long, DeliveryOrder> markNormal(Context context, String[] deliveryOrderIds) throws UserException {
        if (deliveryOrderIds == null || deliveryOrderIds.length == 0) {
            return null;
        }
        List<String> stringDeliveryOrderIds = Arrays.asList(deliveryOrderIds);
        List<Long> longDeliveryOrderIds = new ArrayList<Long>();
        for (String s : stringDeliveryOrderIds) {
            longDeliveryOrderIds.add(Long.valueOf(s));
        }
        List<DeliveryOrder> deliveryOrders = deliveryOrderDao.getDeliveryOrdersByIdList(longDeliveryOrderIds);
        // 因为现在有个定时任务自动扫描配送中的订单，如果超过了三个小时则自动标记为异常了，所以现在不用校验是否超时了
        // deliveryOrderComponent.validateDeliveryOrder(context, deliveryOrders);
        if (CollectionUtils.isEmpty(deliveryOrders)) {
            return null;
        }
        Iterator<DeliveryOrder> iterator = deliveryOrders.iterator();
        while (iterator.hasNext()) {
            DeliveryOrder deliveryOrder = iterator.next();
            Set<Integer> rstIDS = commonDeliveryOrderService.getRstIdsByContext(context);
            if (!rstIDS.contains(deliveryOrder.getRst_id())) {
                throw new UserException("REMOVE_ERROR_100", "您无权限");
            }
        }

        Map<Long, DeliveryOrder> map = new HashMap<Long, DeliveryOrder>();
        try {
            for (String deliveryOrderId : deliveryOrderIds) {
                changeOrderStatusToNormal(deliveryOrderId, context, map);
                List<String> ids = new ArrayList<String>();
                ids.add(deliveryOrderId);
                deliveryOrderComponent.writeDeliveryingOrderIdsToRedis(ids);
            }
        } catch (Exception e) {
            logger.error("更新配送单状态失败", e);
            if (e instanceof UserException) {
                throw e;
            }
            throw new SystemException("DELIVERY_ORDER_ERROR_600", "系统异常，请重试");
        }
        return map;

    }

    /**
     * 将配送单从异常置为正常，该方法会操作DB，将配送单状态修改为正常，同时在配送记录中增加对应的记录
     * 
     * @param deliveryOrderId
     * @param context
     * @param map 该map用来存放置为正常后的配送单信息，key为配送单ID，value为配送单
     * @throws UserException
     */
    private void changeOrderStatusToNormal(String deliveryOrderId, Context context, Map<Long, DeliveryOrder> map)
            throws UserException {
        DeliveryOrder deliveryOrder = deliveryOrderDao.getDeliveryOrdersById(Long.valueOf(deliveryOrderId));
        if (deliveryOrder == null || (!(DeliveryOrderContant.EXCEPTION == deliveryOrder.getStatus()))
                || deliveryOrder.getTaker_id() != context.getUser().getId()) {
            map.put(Long.valueOf(deliveryOrderId), null);
            return;
        }
        List<DeliveryOrderRecord> deliveryOrderRecords = deliveryOrderRecordDao
                .getDeliveryOrderRecordByDeliveryOrderId(Long.valueOf(deliveryOrderId));
        DeliveryOrderRecord deliveryOrderRecord = deliveryOrderRecords.get(deliveryOrderRecords.size() - 1);
        // 如果是系统自动标记的异常
        if (EXCEPTIONMSG.equals(deliveryOrderRecord.getRemark())) {
            throw new UserException("DELIVERY_ORDER_ERROR_600", "配送中已经超过四个小时的单子不能标记回正常");
        }
        // 一期是直接置为配送中
        deliveryOrder.setStatus(DeliveryOrderContant.DELIVERYING);
        deliveryOrder.setTaker_id(context.getUser().getId());
        deliveryOrder.setTaker_mobile(context.getUser().getMobile());
        deliveryOrder.setUpdated_at(new Timestamp(System.currentTimeMillis()));
        deliveryOrder.setEle_order_detail(eleOrderDetailDao.getEleOrderByEleOrderId(deliveryOrder.getEle_order_id())
                .getEle_order_detail());
        deliveryOrderDao.update(deliveryOrder);
        // 修改配送记录表
        deliveryOrderRecord.setCreated_at(new Timestamp(System.currentTimeMillis()));
        deliveryOrderRecord.setTaker_id(context.getUser().getId());
        deliveryOrderRecord.setTaker_mobile(context.getUser().getMobile());
        deliveryOrderRecord.setOperation_id(DeliveryOrderContant.OPERATION_MARKNORMAL);
        deliveryOrderRecord.setOperator_id(context.getUser().getId());
        deliveryOrderRecord.setStatus(DeliveryOrderContant.DELIVERYING);
        deliveryOrderRecordDao.addDeliveryOrderRecord(deliveryOrderRecord);
        List<DeliveryOrder> deliveryOrders = new ArrayList<DeliveryOrder>();
        deliveryOrders.add(deliveryOrder);
        deliveryOrderComponent.addCallRecords(deliveryOrders);
        deliveryOrderComponent.addRstName(deliveryOrders);
        map.put(Long.valueOf(deliveryOrderId), deliveryOrder);
    }

}
