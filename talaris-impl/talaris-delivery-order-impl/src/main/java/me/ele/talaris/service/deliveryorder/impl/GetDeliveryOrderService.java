package me.ele.talaris.service.deliveryorder.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import me.ele.talaris.constant.DeliveryOrderContant;
import me.ele.talaris.context.utils.ContextUtil;

import me.ele.talaris.deliveryorder.dto.DeliveryOrder;
import me.ele.talaris.deliveryorder.persistent.dao.DeliveryOrderDao;
import me.ele.talaris.deliveryorder.persistent.dao.DeliveryOrderRecordDao;
import me.ele.talaris.exception.SystemException;
import me.ele.talaris.exception.UserException;
import me.ele.talaris.model.Context;
import me.ele.talaris.deliveryorder.persistent.eb.DeliveryOrderRecord;
import me.ele.talaris.deliveryorder.dto.DeliveryOrderExInfo;
import me.ele.talaris.deliveryorder.dto.GetDeliveryOrderFilter;
import me.ele.talaris.model.settlement.TheLastSettleInfo;
import me.ele.talaris.redis.CacheLock;
import me.ele.talaris.redis.LockedObject;
import me.ele.talaris.service.deliveryorder.IGetDeliveryOrderService;
import me.ele.talaris.service.settlement.impl.SettlementComponent;
import me.ele.talaris.utils.SerializeUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * 查看配送单
 * 
 * @author zhengwen
 *
 */
@Service
public class GetDeliveryOrderService implements IGetDeliveryOrderService {
    private final static Logger logger = LoggerFactory.getLogger(GetDeliveryOrderService.class);
    private static final Double ONE_HOUR = 60.0 * 60 * 1000;
    @Value("${DeliveryOrderAutoConfirmTimeInterval}")
    private Long AUTO_STATUS_UPDATE_INTERVAL; // ms

    @Autowired
    DeliveryOrderComponent deliveryOrderComponent;

    @Autowired
    DeliveryOrderDao deliveryOrderDao;
    @Autowired
    DeliveryOrderRecordDao deliveryOrderRecordDao;
    @Autowired
    SettlementComponent settlementComponent;

    /**
     * 获取配送单，大接口，该方法实际需要考虑很多，目前仅支持了简易版功能
     * 
     * @param deliveryOrderFilter
     * @param context
     * @return
     * @throws UserException
     */
    @Override
    public List<DeliveryOrder> getDeliveryOrders(GetDeliveryOrderFilter deliveryOrderFilter, Context context,
            String deviceType) throws UserException {

        // 这样来做查询
        // 1：现在该接口只用来查看自己的配送单
        deliveryOrderDao.getDeliveryOrdersByFilter(deliveryOrderFilter.getStatus(), context.getUser().getId(), null,
                deliveryOrderFilter.getPayment_type(), null, null);
        boolean isManager = false;
        try {
            ContextUtil.getMyStationIdByContext(context);
            isManager = true;
        } catch (Throwable e) {
            isManager = false;
        }
        List<DeliveryOrder> deliveryOrders = new ArrayList<DeliveryOrder>();
        List<Long> idList = deliveryOrderComponent.createIdList(deliveryOrderFilter);
        List<Long> eleIdList = deliveryOrderComponent.createEleIdList(deliveryOrderFilter);
        // 根据配送单号查询
        if (!CollectionUtils.isEmpty(idList)) {
            try {
                deliveryOrders = getDeliverOrdersByDeliveryIdList(context, idList, isManager);
                logger.debug("根据ID查询");
            } catch (Exception e) {
                logger.error("根据配送单号查询配送单异常", e);
                throw new SystemException("DELIVERY_ORDER_ERROR_561", "系统异常");
            }
        }
        // 根据饿单号进行查询
        else if (!CollectionUtils.isEmpty(eleIdList)) {
            try {
                deliveryOrders = getDeliverOrdersByEleOrderIdList(context, eleIdList, isManager);
                logger.debug("根据eleID查询");

            } catch (Exception e) {
                logger.error("根据饿单号查询配送单异常", e);
                throw new SystemException("DELIVERY_ORDER_ERROR_561", "系统异常");
            }

        } else {
            deliveryOrders = deliveryOrderDao.getDeliveryOrdersByFilter(deliveryOrderFilter.getStatus(), context
                    .getUser().getId(), null, deliveryOrderFilter.getPayment_type(), null, null);
        }

        if (CollectionUtils.isEmpty(deliveryOrders)) {
            logger.debug("查询结果为空");

            return null;
        }
        // 详情需要饿单json
        if (DeliveryOrderContant.DETAIL_LEVER_ONE.equals(deliveryOrderFilter.getDetail_level())) {
            deliveryOrderComponent.addEleOrderDetailJson(deliveryOrders);
        }
        // 这里为了管理员可以看到自己站点的每个配送员的配送信息，而配送员自己只能看到自己站点下，且taker为自己的配送单信息（目前一期配送员不会转给别人）
        // 将电话记录查询出来添加到配送单里面去，业务需要
        try {
            deliveryOrderComponent.addCallRecords(deliveryOrders);
        } catch (Exception e) {
            logger.error("序列化出错", e);
            throw new SystemException("DELIVERY_ORDER_ERROR_561", "系统异常");
        }
        // deliveryOrderComponent.filterDeliveryOrder(deliveryOrders);
        // 将已经结算过的配送单给过滤掉
        filterSettledDeiliveryOrder(deliveryOrders, context, deliveryOrderFilter.getTaker(), deviceType);
        // 如果是异常订单，则需要添加备注信息
        deliveryOrderComponent.addReMarks(deliveryOrders);
        deliveryOrderComponent.addRstName(deliveryOrders);
        return deliveryOrders;
    }

    private void filterSettledDeiliveryOrder(List<DeliveryOrder> deliveryOrders, Context context, Integer taker,
            String deviceType) throws UserException {
        if (CollectionUtils.isEmpty(deliveryOrders)) {
            return;
        }
        if (!("0".equals(deviceType) || "1".equals(deviceType) || "2".equals(deviceType))) {
            return;
        }
        logger.debug("过滤未结算的单子");

        Map<Integer, List<DeliveryOrder>> rstAndRestaurant = new HashMap<Integer, List<DeliveryOrder>>();
        Set<Integer> set = new HashSet<Integer>();
        for (DeliveryOrder deliveryOrder : deliveryOrders) {

            set.add(deliveryOrder.getRst_id());
        }
        Iterator<Integer> setIterator = set.iterator();
        while (setIterator.hasNext()) {
            Integer rstId = setIterator.next();
            List<DeliveryOrder> partDeliveryOrders = new ArrayList<DeliveryOrder>();
            for (DeliveryOrder deliveryOrder : deliveryOrders) {
                if (deliveryOrder.getRst_id() == rstId) {
                    partDeliveryOrders.add(deliveryOrder);
                }
            }
            rstAndRestaurant.put(rstId, partDeliveryOrders);
        }
        deliveryOrders.clear();
        Iterator<Entry<Integer, List<DeliveryOrder>>> mapIterator = rstAndRestaurant.entrySet().iterator();
        while (mapIterator.hasNext()) {
            Entry<Integer, List<DeliveryOrder>> entry = mapIterator.next();

            TheLastSettleInfo theLastSettleInfo = settlementComponent.getTheLastSettleTimeByTakerId(
                    taker == null ? context.getUser().getId() : taker, context, entry.getKey());
            Timestamp timestamp = theLastSettleInfo.getSettleTime();
            logger.debug("餐厅号为{}", entry.getKey());

            logger.debug("{},{}", timestamp, entry.getValue().size());
            List<DeliveryOrder> everytRstDeliveryOrders = entry.getValue();
            Iterator<DeliveryOrder> iterator = everytRstDeliveryOrders.iterator();
            while (iterator.hasNext()) {
                DeliveryOrder deliveryOrder = iterator.next();
                if (deliveryOrder.getCreated_at().before(timestamp)) {
                    logger.debug("{},{}", timestamp, deliveryOrder.getCreated_at());
                    iterator.remove();
                }
            }
            if (!CollectionUtils.isEmpty(everytRstDeliveryOrders)) {
                deliveryOrders.addAll(everytRstDeliveryOrders);
            }
        }

    }

    /**
     * 根据配送单号查询配送单，同时会判断这些配送单是否在当前人的站点，不在则移除
     * 
     * @param context
     * @param idList
     * @return
     * @throws UserException
     */
    public List<DeliveryOrder> getDeliverOrdersByDeliveryIdList(Context context, List<Long> idList, boolean isManager)
            throws UserException {
        List<DeliveryOrder> deliveryOrders;
        List<DeliveryOrder> resultDeliveryOrders = new ArrayList<DeliveryOrder>();
        deliveryOrders = deliveryOrderDao.getDeliveryOrdersByIdList(idList);
        if (!CollectionUtils.isEmpty(deliveryOrders)) {
            Iterator<DeliveryOrder> iterator = deliveryOrders.iterator();
            while (iterator.hasNext()) {
                DeliveryOrder deliveryOrder = iterator.next();
                if (isManager) {
                    if (context.getStationID().contains(deliveryOrder.getStation_id())) {
                        resultDeliveryOrders.add(deliveryOrder);
                    }
                } else if (context.getStationID().contains(deliveryOrder.getStation_id())
                        && context.getUser().getId() == deliveryOrder.getTaker_id()) {
                    resultDeliveryOrders.add(deliveryOrder);
                }
            }
        }
        return resultDeliveryOrders;
    }

    public List<DeliveryOrder> getDeliverOrdersByDeliveryIdListWithOutAnyValid(Context context, List<Long> idList)
            throws UserException {
        List<DeliveryOrder> deliveryOrders  = new ArrayList<DeliveryOrder>();
        deliveryOrders = deliveryOrderDao.getDeliveryOrdersByIdList(idList);

        return deliveryOrders;
    }

    /**
     * 根据饿单号来获取配送单信息，同时会判断这些配送单是否在当前人的站点，不在则移除
     * 
     * @param context
     * @param eleIdList
     * @return
     * @throws UserException
     */
    private List<DeliveryOrder> getDeliverOrdersByEleOrderIdList(Context context, List<Long> eleIdList,
            boolean isManager) throws UserException {
        List<DeliveryOrder> deliveryOrders;
        deliveryOrders = deliveryOrderDao.getDeliveryOrdersByEleIdList(eleIdList);
        List<DeliveryOrder> resultDeliveryOrders = new ArrayList<DeliveryOrder>();
        if (!CollectionUtils.isEmpty(deliveryOrders)) {
            Iterator<DeliveryOrder> iterator = deliveryOrders.iterator();
            while (iterator.hasNext()) {
                DeliveryOrder deliveryOrder = iterator.next();
                if (isManager) {
                    if (context.getStationID().contains(deliveryOrder.getStation_id())) {
                        resultDeliveryOrders.add(deliveryOrder);
                    }
                } else if (context.getStationID().contains(deliveryOrder.getStation_id())
                        && context.getUser().getId() == deliveryOrder.getTaker_id()) {
                    resultDeliveryOrders.add(deliveryOrder);
                }
            }
        }
        return resultDeliveryOrders;
    }

    /**
     * 根据其他条件进行查询
     * 
     * @param deliveryOrderFilter
     * @param context
     * @param taker_id
     * @param flag
     * @return
     */
    private List<DeliveryOrder> getDeliveryOrdersByOtherFilter(GetDeliveryOrderFilter deliveryOrderFilter,
            Context context, Integer taker_id, int flag, String deviceType) {
        List<DeliveryOrder> deliveryOrders;
        deliveryOrders = deliveryOrderDao.getDeliveryOrdersByFilter(deliveryOrderFilter.getStatus(),
                deliveryOrderFilter.getTaker(), deliveryOrderFilter.getPassed_by(), deliveryOrderFilter
                        .getPayment_type(), deliveryOrderFilter.getFrom_time() == null ? null : new Timestamp(
                        deliveryOrderFilter.getFrom_time()), deliveryOrderFilter.getTo_time() == null ? null
                        : new Timestamp(deliveryOrderFilter.getTo_time()));
        logger.debug("deliveryOrders.size" + deliveryOrders.size());
        if (flag == 1) {
            if (taker_id != null) {
                processDeliveryOrderResult(deliveryOrders, taker_id);
            }
            // 只查询当前站点的配送单
            processMyStationOrders(deliveryOrders, context);
        } else {
            processDeliveryOrderResult(deliveryOrders, context.getUser().getId());

        }
        logger.debug("deviceType===" + deviceType);
        if (!isWebAdministrator(deviceType)) {
            logger.debug("配送员");
            processDeliveryOrderResult(deliveryOrders, context.getUser().getId());
        }
        return deliveryOrders;
    }

    /**
     * 配送员只能查看taker为自己的配送单信息
     * 
     * @param deliveryOrders
     * @param user_id
     */
    private void processDeliveryOrderResult(List<DeliveryOrder> deliveryOrders, Integer user_id) {
        if (user_id == 0) {
            return;
        }
        if (!CollectionUtils.isEmpty(deliveryOrders)) {
            Iterator<DeliveryOrder> iterator = deliveryOrders.iterator();
            while (iterator.hasNext()) {

                DeliveryOrder deliveryOrder = iterator.next();
                if (!(deliveryOrder.getTaker_id() == user_id)) {
                    iterator.remove();
                }
            }
        }
    }

    private void processMyStationOrders(List<DeliveryOrder> deliveryOrders, Context context) {
        if (CollectionUtils.isEmpty(deliveryOrders)) {
            return;
        } else {
            Iterator<DeliveryOrder> iterator = deliveryOrders.iterator();
            while (iterator.hasNext()) {

                DeliveryOrder deliveryOrder = iterator.next();
                if (!(context.getStationID().contains(deliveryOrder.getStation_id()))) {
                    iterator.remove();
                }
            }
        }

    }

    /**
     * 2015-03-07 12:13:16 上线前紧急业务上的BUG，管理员同时以配送员身份登陆APP端的时候能查看到站点下的其他配送员的配送单信息
     * 
     * @param deviceType
     * @return
     */
    private boolean isWebAdministrator(String deviceType) {
        if ("0".equals(deviceType) || "1".equals(deviceType) || "2".equals(deviceType)) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void autoUpdateDeliveryingOrderStatus() {
        Map<String, String> deliveryOrders = deliveryOrderComponent.getDeliveryingOrders();

        if (deliveryOrders != null) {
            for (Map.Entry<String, String> entry : deliveryOrders.entrySet()) {
                String orderId = entry.getKey();
                String orderDetailJson = entry.getValue();

                DeliveryOrderExInfo orderExInfo = SerializeUtil.jsonToBean(orderDetailJson, DeliveryOrderExInfo.class);

                Long orderUpdateTime = orderExInfo.getStatus_change_time().getTime();
                Date now = new Date();
                Long currentTime = now.getTime();
                Long detaTime_ms = currentTime - orderUpdateTime;

                if (AUTO_STATUS_UPDATE_INTERVAL == null) {
                    AUTO_STATUS_UPDATE_INTERVAL = 3 * 60 * 60 * 1000l;
                    logger.info("set auto confirm interval to " + AUTO_STATUS_UPDATE_INTERVAL);
                }

                if (detaTime_ms > AUTO_STATUS_UPDATE_INTERVAL) {
                    autoConfirm(orderId);

                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String strUpdateTime = df.format(orderExInfo.getStatus_change_time());
                    String strCurrentTime = df.format(now);

                    logger.info("订单[" + orderId + "] updateAt: " + strUpdateTime + " current: " + strCurrentTime
                            + ", 配送时间超过" + AUTO_STATUS_UPDATE_INTERVAL + "ms，状态自动更新为已送达.");

                }
            }
        } else {
            logger.info("Redis中没有相关订单数据");
        }
    }

    @CacheLock(lockedPrefix = "OPRERATOR_ORDER_ID")
    public void autoConfirm(@LockedObject String orderId) {
        DeliveryOrder tmpOrder = deliveryOrderDao.getDeliveryOrdersById(Long.valueOf(orderId));

        if (tmpOrder != null) {
            if (tmpOrder.getStatus() != DeliveryOrderContant.DELIVERYING) {
                logger.info("订单[" + orderId + "]状态已被更新.");
                deliveryOrderComponent.rmDeliveryFromRedis(tmpOrder.getId());
                return;
            }
            int rs = deliveryOrderDao.updateDeliveryOrderStatus(Long.valueOf(orderId), DeliveryOrderContant.DELIVERIED);

            if (rs > 0) {
                // 更新配送单状态成功，从redis重删除该条记录，在record表中新增记录
                deliveryOrderComponent.rmDeliveryFromRedis(tmpOrder.getId());
                logger.info("从Redis移除订单[" + orderId + "],状态已自动更新为已送达.");
                deliveryOrderRecordDao.addDeliveryOrderRecord(new DeliveryOrderRecord(tmpOrder,
                        DeliveryOrderContant.SYSTEM_OPERATOR, DeliveryOrderContant.OPERATION_CONFIRM,
                        DeliveryOrderContant.MARK_CONFIRM_MSG));
            } else {
                logger.info("更新订单状态失败，订单ID:" + orderId);
            }
        } else {
            logger.info("无法找到该配送单，请确认配送单id是否正确: " + orderId);
            deliveryOrderComponent.rmDeliveryFromRedis(Long.valueOf(orderId));
        }
    }

    // @Override
    // public Map<Integer, List> getDeliveryOrderListByRstId(List rstIds){
    // return deliveryOrderDao.getDeliveryOrdersByRstIdList(rstIds);
    // }

    @Override
    public void testAddRedisTestOrder() {
        List<DeliveryOrder> orders = deliveryOrderDao.select("where status = ?", 2);
        Map<String, DeliveryOrderExInfo> map = new HashMap<>();
        for (DeliveryOrder order : orders) {
            DeliveryOrderExInfo exInfo = new DeliveryOrderExInfo();
            exInfo.setStatus_change_time(order.getUpdated_at());

            map.put(String.valueOf(order.getId()), exInfo);
        }
        deliveryOrderComponent.writeDeliveryingOrderIdsToRedis(map);
    }

    @Override
    public Map<String, String> testGetRedisOrder() {
        return deliveryOrderComponent.getDeliveryingOrders();
    }

    //
    // @Override
    // public Map<Integer, List> getDeliveryOrderListByRstId(List rstIds) {
    // // TODO Auto-generated method stub
    // return null;
    // }

    public boolean needManuallyOrderCustomerNotifiedThroughMsg(Long customerMobile){
        Long deliveryCount = deliveryOrderDao.getDelvieryTimesByCustomerMobile(customerMobile, Integer.valueOf(DeliveryOrderContant.SOURCE_FROM_MANAULLY));
        if(deliveryCount > 1l){
            return true;
        }

        return  false;
    }
}
