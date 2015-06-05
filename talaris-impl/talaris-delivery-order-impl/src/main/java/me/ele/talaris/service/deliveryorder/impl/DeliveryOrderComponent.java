package me.ele.talaris.service.deliveryorder.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import me.ele.talaris.constant.Constant;
import me.ele.talaris.constant.DeliveryOrderContant;
import me.ele.talaris.dao.CallTaskInfoDao;
import me.ele.talaris.deliveryorder.dto.DeliveryOrder;
import me.ele.talaris.deliveryorder.persistent.dao.DeliveryOrderDao;
import me.ele.talaris.deliveryorder.persistent.dao.DeliveryOrderRecordDao;
import me.ele.talaris.dao.EleOrderDetailDao;
import me.ele.talaris.dao.RestaurantDao;
import me.ele.talaris.dao.UserStationRoleDao;
import me.ele.talaris.deliveryorder.dto.DeliveryOrderExInfo;
import me.ele.talaris.deliveryorder.dto.GetDeliveryOrderFilter;
import me.ele.talaris.exception.UserException;
import me.ele.talaris.model.CallTaskInfo;
import me.ele.talaris.model.CallTaskInfoEx;
import me.ele.talaris.model.Context;

import me.ele.talaris.deliveryorder.persistent.eb.DeliveryOrderRecord;
import me.ele.talaris.model.EleOrderDetail;
import me.ele.talaris.model.Restaurant;
import me.ele.talaris.model.UserStationRole;
import me.ele.talaris.napos.service.INaposService;
import me.ele.talaris.redis.RedisClient;
import me.ele.talaris.service.settlement.impl.SettlementComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

public class DeliveryOrderComponent {

    private final static Logger WAITINGTODELIVERYINGLOGGER = LoggerFactory.getLogger("wait_to_delivery");
    // 待配送单的单子ID，方便定时任务批量跑三个小时后还在代配送的单子
    public static final String WAIT_TO_DELIVERORDER_IDS = "WAIT_TO_DELIVERORDER_IDS";
    public static final String DELIVERYING_ORDER_IDS = "DELIVERYING_ORDER_IDS";

    private final static Logger DELIVERYINGORDERIDSLOGGER = LoggerFactory.getLogger("deliverying_orders");
    private RedisClient redisClient;

    public RedisClient getRedisClient() {
        return redisClient;
    }

    public void setRedisClient(RedisClient redisClient) {
        this.redisClient = redisClient;
    }

    private EleOrderDetailDao eleOrderDetailDao;
    private DeliveryOrderDao deliveryOrderDao;

    private DeliveryOrderRecordDao deliveryOrderRecordDao;

    private INaposService talarisNaposService;

    private UserStationRoleDao userStationRoleDao;
    private CallTaskInfoDao callTaskInfoDao;
    private SettlementComponent settlementComponent;
    private int expiredHours;
    private int markConfirmDeliveried;

    private double bookedOrderDisplayTime;

    public double getBookedOrderDisplayTime() {
        return bookedOrderDisplayTime;
    }

    public void setBookedOrderDisplayTime(double bookedOrderDisplayTime) {
        this.bookedOrderDisplayTime = bookedOrderDisplayTime;
    }

    private RestaurantDao restaurantDao;

    public RestaurantDao getRestaurantDao() {
        return restaurantDao;
    }

    public void setRestaurantDao(RestaurantDao restaurantDao) {
        this.restaurantDao = restaurantDao;
    }

    public int getMarkConfirmDeliveried() {
        return markConfirmDeliveried;
    }

    public void setMarkConfirmDeliveried(int markConfirmDeliveried) {
        this.markConfirmDeliveried = markConfirmDeliveried;
    }

    public int getExpiredHours() {
        return expiredHours;
    }

    public void setExpiredHours(int expiredHours) {
        this.expiredHours = expiredHours;
    }

    public EleOrderDetailDao getEleOrderDetailDao() {
        return eleOrderDetailDao;
    }

    public void setEleOrderDetailDao(EleOrderDetailDao eleOrderDetailDao) {
        this.eleOrderDetailDao = eleOrderDetailDao;
    }

    public DeliveryOrderDao getDeliveryOrderDao() {
        return deliveryOrderDao;
    }

    public void setDeliveryOrderDao(DeliveryOrderDao deliveryOrderDao) {
        this.deliveryOrderDao = deliveryOrderDao;
    }

    public DeliveryOrderRecordDao getDeliveryOrderRecordDao() {
        return deliveryOrderRecordDao;
    }

    public void setDeliveryOrderRecordDao(DeliveryOrderRecordDao deliveryOrderRecordDao) {
        this.deliveryOrderRecordDao = deliveryOrderRecordDao;
    }

    public INaposService getTalarisNaposService() {
        return talarisNaposService;
    }

    public void setTalarisNaposService(INaposService talarisNaposService) {
        this.talarisNaposService = talarisNaposService;
    }

    public UserStationRoleDao getUserStationRoleDao() {
        return userStationRoleDao;
    }

    public void setUserStationRoleDao(UserStationRoleDao userStationRoleDao) {
        this.userStationRoleDao = userStationRoleDao;
    }

    public CallTaskInfoDao getCallTaskInfoDao() {
        return callTaskInfoDao;
    }

    public void setCallTaskInfoDao(CallTaskInfoDao callTaskInfoDao) {
        this.callTaskInfoDao = callTaskInfoDao;
    }

    public SettlementComponent getSettlementComponent() {
        return settlementComponent;
    }

    public void setSettlementComponent(SettlementComponent settlementComponent) {
        this.settlementComponent = settlementComponent;
    }

    public static final Logger logger = LoggerFactory.getLogger(DeliveryOrderComponent.class);

    /**
     * 改变配送单配送状态，该方法同时会对DB进行操作，改变配送单状态，以及增加配送记录信息
     * 
     * @param context
     * @param deliveryOrder
     * @param status
     * 
     * @param needToTellNapos 是否需要通napos
     * @throws UserException
     * @throws Exception
     */
    // 该方法可能会抛出dataAccessException，但是没有抓取，直接往上抛出，最后会被处理成系统异常
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void changeDeliveryOrderStatus(Context context, DeliveryOrder deliveryOrder, int status,
            boolean needToTellNapos, String remark) throws UserException {
        deliveryOrder.setStatus(status);
        deliveryOrder.setTaker_id(context.getUser().getId());
        deliveryOrder.setTaker_mobile(context.getUser().getMobile());
        deliveryOrder.setUpdated_at(new Timestamp(System.currentTimeMillis()));
        EleOrderDetail eleOrderDetail = eleOrderDetailDao.getEleOrderByEleOrderId(deliveryOrder.getEle_order_id());
        if (eleOrderDetail != null) {
            deliveryOrder.setEle_order_detail(eleOrderDetail.getEle_order_detail());
        }
        deliveryOrderDao.update(deliveryOrder);
        // 增加配送记录信息
        // 根据配送单单号获取该单在配送记录表中的最后一个配送状态

        List<DeliveryOrderRecord> deliveryOrderRecordInDbs = deliveryOrderRecordDao
                .getLastDeliveryOrderRecordByDeliveryOrderId(Long.valueOf(deliveryOrder.getId()));
        DeliveryOrderRecord deliveryOrderRecord = CollectionUtils.isEmpty(deliveryOrderRecordInDbs) ? new DeliveryOrderRecord()
                : deliveryOrderRecordInDbs.get(0);
        // remark不能为空
        deliveryOrderRecord.setRemark(StringUtils.isEmpty(remark) ? "" : remark);
        deliveryOrderRecord.setDelivery_order_id(deliveryOrder.getId());
        deliveryOrderRecord.setEle_order_id(deliveryOrder.getEle_order_id());
        deliveryOrderRecord.setCreated_at(new Timestamp(System.currentTimeMillis()));
        deliveryOrderRecord.setTaker_id(context.getUser().getId());
        deliveryOrderRecord.setTaker_mobile(context.getUser().getMobile());
        deliveryOrderRecord.setStation_id(deliveryOrder.getStation_id());
        deliveryOrderRecord.setStatus(status);
        if (DeliveryOrderContant.EXCEPTION == status) {
            deliveryOrderRecord.setoperation_id(DeliveryOrderContant.OPERATION_MARKEXCEPTION);

        } else if (DeliveryOrderContant.DELIVERIED == status) {
            deliveryOrderRecord.setoperation_id(DeliveryOrderContant.OPERATION_CONFIRM);
        } else if (DeliveryOrderContant.DELIVERYING == status) {
            deliveryOrderRecord.setoperation_id(DeliveryOrderContant.OPERATION_DELIVERYING);
        } else if (DeliveryOrderContant.WAITTO_DELIVERY == status) {
            deliveryOrderRecord.setoperation_id(DeliveryOrderContant.OPERATION_CANCEL);
        }
        deliveryOrderRecord.setOperator_id(context.getUser().getId());
        deliveryOrderRecordDao.addDeliveryOrderRecord(deliveryOrderRecord);
        List<DeliveryOrder> deliveryOrders = new ArrayList<DeliveryOrder>();
        deliveryOrders.add(deliveryOrder);
        // 若不是标记异常，则需要返回给napos系统配送单目前状态
        if (!(DeliveryOrderContant.EXCEPTION == status) && needToTellNapos) {
            try {
                tellNaposEleOrderStatus(context, deliveryOrders, status);
            } catch (Exception e) {
                logger.error("回调napos失败", e);
            }
        }

        List<DeliveryOrder> orders = new ArrayList<DeliveryOrder>();
        addCallRecords(deliveryOrders);
    }

    /**
     * 
     * @param context
     * @param deliveryOrders
     * @param status
     * @throws UserException
     * @throws Exception
     * 告诉napos当前饿单的状态
     * 
     * case 2:
     * tStatus = "delivering";
     * break;
     * case 3:
     * tStatus = "success";
     * break;
     * case 4:
     * tStatus = "failed";
     * break;
     */
    public void tellNaposEleOrderStatus(Context context, List<DeliveryOrder> deliveryOrders, int status)
            throws UserException {
        for (DeliveryOrder deliveryOrder : deliveryOrders) {
            talarisNaposService.updateEleOrderStatus(String.valueOf(deliveryOrder.getEle_order_id()),
                    String.valueOf(deliveryOrder.getId()), status, context.getUser().getName(),
                    String.valueOf(context.getUser().getMobile()));
        }
    }

    /**
     * 该方法供查询配送单大接口调用，因为接口可能传入进来的是deliverOrderId或者是deliverOrderIds;该方法是直接将接口传入的这两个转化为了一个IDLIST
     * 
     * @param deliveryOrderFilter
     * @return
     */
    public List<Long> createIdList(GetDeliveryOrderFilter deliveryOrderFilter) {
        List<Long> idList = new ArrayList<Long>();
        if (!StringUtils.isEmpty(deliveryOrderFilter.getId())) {
            idList.add(Long.valueOf(deliveryOrderFilter.getId()));
        }
        if (deliveryOrderFilter.getId_list() != null) {
            for (String s : deliveryOrderFilter.getId_list()) {
                idList.add(Long.valueOf(s));

            }
        }
        return idList;
    }

    /**
     * 该方法供查询配送单大接口调用，因为接口可能传入进来的是EleOrderId或者是EleOrderIds;该方法是直接将接口传入的这两个转化为了一个IDLIST
     * 
     * @param deliveryOrderFilter
     * @return
     */
    public List<Long> createEleIdList(GetDeliveryOrderFilter deliveryOrderFilter) {
        List<Long> idList = new ArrayList<Long>();
        if (!StringUtils.isEmpty(deliveryOrderFilter.getEle_order_id())) {
            idList.add(Long.valueOf(deliveryOrderFilter.getEle_order_id()));
        }
        if (deliveryOrderFilter.getEle_order_id_list() != null) {
            for (String s : deliveryOrderFilter.getEle_order_id_list()) {
                idList.add(Long.valueOf(s));

            }
        }
        return idList;
    }

    /**
     * 判断是否是老板查看配送单,之前该方法没加上 deviceType 参数
     * 
     * @param deliveryOrderFilter
     * @param context
     * @return
     */
    public int isAdministratorWatch(GetDeliveryOrderFilter deliveryOrderFilter, Context context, String deviceType) {

        int flag = 0;
        Integer passed_by = deliveryOrderFilter.getPassed_by();
        Integer taker_id = deliveryOrderFilter.getTaker();

        if (!(passed_by == null && taker_id == null)) {
            // 判断是否是管理员
            for (UserStationRole userStationRole : context.getUserStationRoles()) {
                if (userStationRole.getRole_id() == 1 && isWebAdministrator(deviceType)) {
                    return 1;
                }
            }
            List<Integer> takeStationIds = userStationRoleDao.listStationIdByUserId(taker_id);
            if (CollectionUtils.isEmpty(takeStationIds)) {
                return 0;
            }
            if (takeStationIds.contains(context.getStationID())) {
                flag = 1;
            }
        } else {
            for (UserStationRole userStationRole : context.getUserStationRoles()) {
                if (userStationRole.getRole_id() == 1 && isWebAdministrator(deviceType)) {
                    return 1;
                }
            }
        }
        return flag;

    }

    /**
     * 管理员同时以配送员身份登陆APP端的时候能查看到站点下的其他配送员的配送单信息
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

    /**
     * 将EleOrderDetail放入deliveryOrder对象中
     * 
     * @param deliveryOrders
     */
    public void addEleOrderDetailJson(List<DeliveryOrder> deliveryOrders) {
        if (CollectionUtils.isEmpty(deliveryOrders)) {
            return;
        }
        EleOrderDetail tEleOrderDetail = null;
        Iterator<DeliveryOrder> iterator = deliveryOrders.iterator();
        while (iterator.hasNext()) {
            DeliveryOrder deliveryOrder = iterator.next();
            tEleOrderDetail = eleOrderDetailDao.getEleOrderByEleOrderId(deliveryOrder.getEle_order_id());
            if (tEleOrderDetail != null) {
                deliveryOrder.setEle_order_detail(tEleOrderDetail.getEle_order_detail());
            }
            // 这里正常情况下是不会出现有配送单信息，没有对应的饿单信息的数据，但是不排除异常
            // 如果有异常，这里做的是删除
            else {
                if (deliveryOrder.getSource() == DeliveryOrderContant.SOURCE_FROM_ELEME) {
                    iterator.remove();
                } else {
                    continue;
                }
            }
        }

    }

    /**
     * 将电话通知信息放入DeliveryOrder中
     * 
     * @param deliveryOrders
     */
    // 该方法里面调用getCallTaskInfoByDeliveryOrderId方法的时候可能会抛出Exception ，不用处理，直接继续往上抛出
    public void addCallRecords(List<DeliveryOrder> deliveryOrders) {
        if (CollectionUtils.isEmpty(deliveryOrders)) {
            return;
        }
        for (DeliveryOrder deliveryOrder : deliveryOrders) {
            List<CallTaskInfo> callTaskInfos = callTaskInfoDao.getCallTaskInfoByDeliveryOrderId(deliveryOrder.getId());
            transformCallStatus(callTaskInfos);
            List<CallTaskInfoEx> callTaskInfoExs = changeCallTaskInfoToEx(callTaskInfos);
            deliveryOrder.setCall_records((CollectionUtils.isEmpty(callTaskInfoExs) ? new ArrayList<CallTaskInfoEx>()
                    : callTaskInfoExs));
        }
    }

    /**
     * 对电话记录进行DB到外界显示的一个转化
     * db 里面是
     * initial = 0
     * sending = 1
     * success = 2
     * fail = 3
     * partial_success = 4
     * timeout = 5
     * 
     * 前端显示是：2:用户已接听；3拨打失败；4，拨打成功，用户未接听；5 超时
     */

    public void transformCallStatus(List<CallTaskInfo> callTaskInfos) {

        if (CollectionUtils.isEmpty(callTaskInfos)) {
            return;
        }
        Iterator<CallTaskInfo> iterator = callTaskInfos.iterator();
        while (iterator.hasNext()) {
            CallTaskInfo callTaskInfo = iterator.next();
            int status = callTaskInfo.getStatus();
            if (status == 1 || status == 0) {
                iterator.remove();
            } else {
                changeCallTaskInfo(callTaskInfo);
            }

        }

    }

    private void changeCallTaskInfo(CallTaskInfo callTaskInfo) {
        int status = callTaskInfo.getStatus();
        switch (status) {
            case 2:
                callTaskInfo.setError_log("用户已接听");
                break;
            case 3:
                callTaskInfo.setError_log("拨打失败");
                break;
            case 4:
                callTaskInfo.setError_log("拨打成功，用户未接听");
                break;
            case 5:
                callTaskInfo.setError_log("超时");
                break;
            default:
                break;
        }

    }

    private List<CallTaskInfoEx> changeCallTaskInfoToEx(List<CallTaskInfo> callTaskInfos) {
        List<CallTaskInfoEx> callTaskInfoExs = new ArrayList<CallTaskInfoEx>();
        if (CollectionUtils.isEmpty(callTaskInfos)) {
            return null;
        }
        for (CallTaskInfo callTaskInfo : callTaskInfos) {
            callTaskInfoExs.add(new CallTaskInfoEx(callTaskInfo));
        }
        return callTaskInfoExs;
    }

    /**
     * 
     * @param eleOrderDetail
     * @param type 1获取的是饿单bookedTime,2;获取的饿单创建时间
     * @return
     */
    public Long getTime(String eleOrderDetail, int type) {
        if (Constant.one == type) {
            int index = eleOrderDetail.indexOf(DeliveryOrderContant.BOOKEDTIME);
            String part = eleOrderDetail.substring(index + DeliveryOrderContant.BOOKEDTIME.length() + 1,
                    eleOrderDetail.length());
            String bookedTime = part.trim().substring(0, part.indexOf(","));
            return Long.valueOf(bookedTime) * 1000;
        } else {
            int index = eleOrderDetail.indexOf(DeliveryOrderContant.CREATETIME);
            String part = eleOrderDetail.substring(index + DeliveryOrderContant.CREATETIME.length() + 1,
                    eleOrderDetail.length());
            String createTime = part.trim().substring(0, part.indexOf(","));
            return Long.valueOf(createTime) * 1000;
        }

    }

    /**
     * 做六个小时时间限制
     * 
     * @param allDeliveryOrders
     */
    public void filterDeliveryOrder(List<DeliveryOrder> allDeliveryOrders) {
        if (CollectionUtils.isEmpty(allDeliveryOrders)) {
            return;
        }
        Iterator<DeliveryOrder> iterator = allDeliveryOrders.iterator();
        while (iterator.hasNext()) {
            DeliveryOrder deliveryOrder = iterator.next();
            // 普通订单
            if (deliveryOrder.getBooked_time() == null) {
                Timestamp eleCreatedTime = deliveryOrder.getEle_created_time();
                Timestamp now = new Timestamp(System.currentTimeMillis());
                // 下单超过六个小时,删除该单
                if (now.getTime() - eleCreatedTime.getTime() > expiredHours * 60 * 60 * 1000l) {
                    iterator.remove();
                }
            }
            // 预订单
            else {
                Timestamp bookedTime = deliveryOrder.getBooked_time();
                Timestamp now = new Timestamp(System.currentTimeMillis());
                // 下单超过六个小时,删除该单
                if ((now.getTime() - bookedTime.getTime() > expiredHours * 60 * 60 * 1000l && now.getTime()
                        - bookedTime.getTime() > 0)
                        || (bookedTime.getTime() - now.getTime() > 0 && bookedTime.getTime() - now.getTime() > bookedOrderDisplayTime * 60 * 60 * 1000l)) {
                    iterator.remove();
                }
            }
        }

    }

    public void addReMarks(List<DeliveryOrder> deliveryOrders) {
        for (DeliveryOrder deliveryOrder : deliveryOrders) {

            if (deliveryOrder.getStatus() == DeliveryOrderContant.EXCEPTION) {
                DeliveryOrderRecord deliveryOrderRecord = deliveryOrderRecordDao
                        .getExceptionDeliveryOrderRecord(deliveryOrder.getId());
                if (deliveryOrderRecord != null) {
                    deliveryOrder.setException_remark(deliveryOrderRecord.getRemark());
                }
            }
            if (deliveryOrder.getStatus() == DeliveryOrderContant.DELIVERIED) {
                List<DeliveryOrderRecord> deliveryOrderRecords = deliveryOrderRecordDao
                        .getLastDeliveryOrderRecordByDeliveryOrderId(deliveryOrder.getId());
                if (!CollectionUtils.isEmpty(deliveryOrderRecords)) {
                    deliveryOrder.setException_remark(deliveryOrderRecords.get(0).getRemark());
                }
            }
        }
    }

    // /**
    // * 验证超时，如果超时且为配送中，则直接标记为异常，并抛出异常给前端单号
    // *
    // * @param context
    // * @param deliveryOrders
    // * @throws UserException
    // */
    // public void validateDeliveryOrder(Context context, List<DeliveryOrder> deliveryOrders) throws UserException {
    // // List<String> ids = validateDeliveryOrderTTl(context, deliveryOrders);
    // if (!CollectionUtils.isEmpty(ids)) {
    // logger.debug("case2");
    // throw new SystemException("TTL_ERROR_100", "该配送单在您手中已经超过了" + markExceptionHours + "个小时或者已经被结算了", ids);
    // }
    // }

    // @Transactional
    // public List<String> validateDeliveryOrderTTl(Context context, List<DeliveryOrder> deliveryOrders)
    // throws UserException {
    //
    // if (CollectionUtils.isEmpty(deliveryOrders)) {
    // return null;
    // }
    // List<String> ids = new ArrayList<String>();
    // Timestamp Hour12Ago = new Timestamp(System.currentTimeMillis() - markExceptionHours * 1000l * Times.ONE_HOUR);
    // Timestamp timestamp = settlementComponent.getTheLastSettleTimeByTakerId(context.getUser().getId(), context)
    // .getSettleTime();
    // Timestamp start = Hour12Ago.before(timestamp) ? timestamp : Hour12Ago;
    // for (DeliveryOrder deliveryOrder : deliveryOrders) {
    // logger.debug("test markexception1");
    // EleOrderDetail eleOrderDetail = eleOrderDetailDao.getEleOrderByEleOrderId(deliveryOrder.getEle_order_id());
    // String eleOrderDetailString = eleOrderDetail.getEle_order_detail();
    // // 这里为什么不用序列化反序列化的原因是：到napos拉取饿单回来的各种时间是秒，而不是毫秒，所以只能通过字符串的方式来解析。
    // // 判断是否是预订单,如果是以bookedTime为准
    // if (eleOrderDetailString.indexOf(DeliveryOrderContant.ISBOOKED) != -1) {
    // logger.debug("test markexception2");
    // Timestamp bookedTime = new Timestamp(getTime(eleOrderDetailString, 1));
    // // 下单超过六个小时,删除该单
    // if (deliveryOrder.getStatus() == DeliveryOrderContant.DELIVERYING
    // && bookedTime.getTime() + markExceptionHours * 1000l * Times.ONE_HOUR < System
    // .currentTimeMillis()) {
    // try {
    // logger.debug("test markexception3,时间是{}", expiredHours);
    // // 若用户将
    // changeDeliveryOrderStatus(context, deliveryOrder, DeliveryOrderContant.EXCEPTION, false,
    // DeliveryOrderContant.MARKEXCEPTIONMSG);
    // } catch (UserException e) {
    // // 实际上基本不可能抛出这个异常，因为napos根本没通知
    // }
    // ids.add(String.valueOf(deliveryOrder.getId()));
    // }
    // } else {
    // if (start.after(deliveryOrder.getCreated_at())) {
    // try {
    // if (deliveryOrder.getStatus() == DeliveryOrderContant.DELIVERYING) {
    // // 若用户将
    // changeDeliveryOrderStatus(context, deliveryOrder, DeliveryOrderContant.EXCEPTION, false,
    // DeliveryOrderContant.MARKEXCEPTIONMSG);
    // }
    // } catch (UserException e) {
    // // 实际上基本不可能抛出这个异常，因为napos根本没通知
    // }
    // ids.add(String.valueOf(deliveryOrder.getId()));
    // }
    // }
    // }
    // return ids;
    //
    // }

    public List<String> getDeilvieryOrderIDS(List<DeliveryOrder> deliveryOrders) {
        if (CollectionUtils.isEmpty(deliveryOrders)) {
            return null;
        }
        List<String> ids = new ArrayList<String>();
        for (DeliveryOrder deliveryOrder : deliveryOrders) {
            ids.add(String.valueOf(deliveryOrder.getId()));
        }
        return ids;

    }

    /**
     * 往redis里面写入待配送的单子单号
     * 
     * @param ids
     */
    public void writeWaitingToDeliveryOrderIdsToRedis(List<String> ids) {
        try {
            Map<String, DeliveryOrderExInfo> map = new HashMap<String, DeliveryOrderExInfo>();
            for (String id : ids) {
                map.put(id, new DeliveryOrderExInfo(new Timestamp(System.currentTimeMillis())));
            }
            redisClient.hmset(WAIT_TO_DELIVERORDER_IDS, map);
        } catch (Throwable e) {
            WAITINGTODELIVERYINGLOGGER.error("往redis里写入待配送的单子单号失败", e);
            WAITINGTODELIVERYINGLOGGER.error("代配送单子写入redis失败,各个单子的ID为:", ids.toString());
        }
    }

    /**
     * 从redis里面删除待配送的单子单号
     * 
     * @param ids
     */
    public void deleteWaitingToDeliveryOrderIdsInRedis(List<String> ids) {
        try {

            redisClient.hdel(WAIT_TO_DELIVERORDER_IDS, ids.toArray(new String[ids.size()]));
        } catch (Throwable e) {
            WAITINGTODELIVERYINGLOGGER.error("从redis里面删除待配送的单子失败", e);
            WAITINGTODELIVERYINGLOGGER.error("删除redis里面待配送的单子失败，各个单子的ID为:", ids.toString());
        }
    }

    /**
     * 从redis里查询待配送的订单列表
     *
     * @return
     */
    public Map<String, String> getWatingToDeliveryOrders() {
        try {
            return redisClient.hGetAll(WAIT_TO_DELIVERORDER_IDS);
        } catch (Throwable e) {
            WAITINGTODELIVERYINGLOGGER.error("从redis里面查询待配送的订单列表失败", e);
        }
        return null;
    }

    /**
     * 往redis里面写入配送中的单子单号
     * 
     * @param ids
     */
    public void writeDeliveryingOrderIdsToRedis(List<String> ids) {
        try {
            Map<String, DeliveryOrderExInfo> map = new HashMap<String, DeliveryOrderExInfo>();
            for (String id : ids) {
                DeliveryOrder deliveryOrder = deliveryOrderDao.getDeliveryOrdersById(Long.valueOf(id));
                // long eleOrderId = deliveryOrder.getEle_order_id();
                // EleOrderDetail eleOrderDetail = eleOrderDetailDao.getEleOrderByEleOrderId(eleOrderId);
                // String eleOrderDetailJson = eleOrderDetail.getEle_order_detail();
                // 如果是预订单
                // if (eleOrderDetailJson.indexOf(DeliveryOrderContant.ISBOOKED) != -1) {
                if (deliveryOrder.getBooked_time() != null) {
                    long writeTime = 0l;
                    // long bookedTime = getTime(eleOrderDetailJson, 1);
                    long bookedTime = deliveryOrder.getBooked_time().getTime();
                    if (System.currentTimeMillis() - bookedTime > 0) {
                        writeTime = System.currentTimeMillis();
                    } else {
                        writeTime = bookedTime;
                    }
                    map.put(id, new DeliveryOrderExInfo(new Timestamp(writeTime)));
                } else {
                    map.put(id, new DeliveryOrderExInfo(new Timestamp(System.currentTimeMillis())));
                }
            }
            writeDeliveryingOrderIdsToRedis(map);
        } catch (Throwable e) {
            DELIVERYINGORDERIDSLOGGER.error("往redis里写入配送中的单子单号失败", e);
            DELIVERYINGORDERIDSLOGGER.error("配送中的单子写入redis失败,各个单子的ID为:", ids.toString());
        }
    }

    /**
     * 往redis里面写入配送中的单子单号
     *
     * @param ids
     */
    public void writeDeliveryingOrderIdsToRedis(Map<String, DeliveryOrderExInfo> map) {
        try {
            redisClient.hmset(DELIVERYING_ORDER_IDS, map);
        } catch (Throwable e) {
            DELIVERYINGORDERIDSLOGGER.error("往redis里写入配送中的单子单号失败", e);
        }
    }

    /**
     * 从redis里面删除配送中的单子单号
     * 
     * @param ids
     */
    public void deleteDeliveryingOrderIdsInRedis(List<String> ids) {
        try {
            redisClient.hdel(DELIVERYING_ORDER_IDS, ids.toArray(new String[ids.size()]));
        } catch (Throwable e) {
            DELIVERYINGORDERIDSLOGGER.error("从redis删除配送中的单子失败", e);
            DELIVERYINGORDERIDSLOGGER.error("配送中的单子写入redis失败,各个单子的ID为:", ids.toString());
        }
    }

    /**
     * 从redis里查询配送中的订单列表
     *
     * @return
     */
    public Map<String, String> getDeliveryingOrders() {
        try {
            return redisClient.hGetAll(DELIVERYING_ORDER_IDS);
        } catch (Throwable e) {
            WAITINGTODELIVERYINGLOGGER.error("从redis里面查询配送中的单子失败", e);
        }
        return null;
    }

    public void rmDeliveryFromRedis(Long orderId) {
        Map<String, String> currOrders = redisClient.hGetAll(DELIVERYING_ORDER_IDS);
        for (Map.Entry<String, String> entry : currOrders.entrySet()) {
            if (entry.getKey().equals(orderId.toString())) {
                redisClient.hdel(DELIVERYING_ORDER_IDS, entry.getKey());
            }
        }
    }

    public void addRstName(List<DeliveryOrder> deliveryOrders) {
        if (CollectionUtils.isEmpty(deliveryOrders)) {
            return;
        }
        for (DeliveryOrder deliveryOrder : deliveryOrders) {
            Restaurant restaurant = restaurantDao.getRestaurantByEleId(deliveryOrder.getRst_id());
            logger.debug("餐厅名称为：{}", restaurant.getName());
            deliveryOrder.setRst_name(restaurant.getName());
        }
    }

    public int insertDeliveryOrderRecord(DeliveryOrderRecord orderRecord) {
        return deliveryOrderRecordDao.insert(orderRecord);
    }

    public Restaurant getRestaurantByEleRstId(int eleRstId) {
        return restaurantDao.getRestaurantByEleId(eleRstId);
    }

    public void setEleOrderDetail(DeliveryOrder order) {
        EleOrderDetail eleOrderDetail = eleOrderDetailDao.getEleOrderByEleOrderId(order.getEle_order_id());
        if (eleOrderDetail == null) {
            logger.error("无法查找到订单，额单号：" + order.getEle_order_id());
        }
        order.setEle_order_detail(eleOrderDetail.getEle_order_detail());
    }
}
