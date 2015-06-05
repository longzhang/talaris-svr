package me.ele.talaris.service.deliveryorder.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import me.ele.talaris.constant.DeliveryOrderContant;
import me.ele.talaris.deliveryorder.dto.DeliveryOrder;
import me.ele.talaris.deliveryorder.persistent.dao.DeliveryOrderDao;
import me.ele.talaris.deliveryorder.persistent.dao.DeliveryOrderRecordDao;
import me.ele.talaris.dao.EleOrderDetailDao;
import me.ele.talaris.dao.RestaurantDao;
import me.ele.talaris.dao.StationDao;
import me.ele.talaris.dao.StationRestaurantDao;
import me.ele.talaris.deliveryorder.dto.AddressResponse;
import me.ele.talaris.deliveryorder.dto.CompressUrlResponse;
import me.ele.talaris.exception.ExceptionCode;
import me.ele.talaris.exception.ExceptionFactory;
import me.ele.talaris.exception.SystemException;
import me.ele.talaris.exception.UserException;
import me.ele.talaris.hermes.service.IHermesService;
import me.ele.talaris.model.Context;
import me.ele.talaris.deliveryorder.persistent.eb.DeliveryOrderRecord;
import me.ele.talaris.model.Restaurant;
import me.ele.talaris.model.Station;
import me.ele.talaris.napos.model.TEleOrder;
import me.ele.talaris.napos.service.INaposService;
import me.ele.talaris.redis.CacheLock;
import me.ele.talaris.redis.LockedObject;
import me.ele.talaris.redis.RedisClient;
import me.ele.talaris.redis.RedisLock;
import me.ele.talaris.service.deliveryorder.ICreateDeliveryOrderService;
import me.ele.talaris.utils.HttpRequest;
import me.ele.talaris.utils.SerializeUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import redis.clients.jedis.JedisPool;

/**
 * 创建配送中的配送单，包括扫码和拉取批量勾选创建
 * 
 * @author zhengwen
 *
 */
@Service
public class CreateDeliveryOrderService implements ICreateDeliveryOrderService {
    private final static Logger logger = LoggerFactory.getLogger(CreateDeliveryOrderService.class);

    @Value(value = "${map.url}")
    private String mapUrl;

    @Value(value = "${location.url}")
    private String locationUrl;

    @Autowired
    HttpRequest httpRequest;
    @Autowired
    private DeliveryOrderDao deliveryOrderDao;

    @Autowired
    private DeliveryOrderRecordDao deliveryOrderRecordDao;
    @Autowired
    private EleOrderDetailDao eleOrderDetailDao;
    @Autowired
    IHermesService coffeeHermesService;

    @Autowired
    INaposService talarisNaposService;
    @Autowired
    StationRestaurantDao stationRestaurantDao;

    @Autowired
    DeliveryOrderComponent deliveryOrderComponent;

    @Autowired
    RedisClient redisClient;

    @Autowired
    JedisPool jedisPool;
    @Autowired
    CommonDeliveryOrderService commonDeliveryOrderService;
    @Autowired
    RestaurantDao restaurantDao;
    @Autowired
    StationDao stationDao;
    @Autowired
    GetDeliveryOrderService getDeliveryOrderService;

    /**
     * 创建配送单，分为扫码创建，或者是批量选择然后确认创建
     * 
     * @param context
     * @param eleOrderIds
     * @param actionType 如果为扫码拉取订单，则直接更新其状态为配送中
     * @return
     * @throws UserException
     */
    @Override
    public List<DeliveryOrder> fetchDeliveryOrders(Context context, String[] eleOrderIds, String actionType)
            throws UserException {
        // 如果是配送员自己在页面选择配送单，并确定，则需要对传进来的单号进行校验，看是不是属于这个配送员对应的站点下的配送单
        List<DeliveryOrder> deliveryOrders = new ArrayList<DeliveryOrder>();
        // 扫码进行创建,只会给扫描成功的行
        if (DeliveryOrderContant.FETCH_ORDER_ACTION_TYPE_BARCODE.equals(actionType)) {
            // deliveryOrders = fetchDeliveryOrdersByBarCode(context, eleOrderIds, actionType);

        }// 配送员批量选择后点击确认
        else if (DeliveryOrderContant.FETCH_ORDER_ACTION_TYPE_LIST.endsWith(actionType)) {
            deliveryOrders = fetchDeliveryOrdersList(context, eleOrderIds, actionType);
        }
        if (CollectionUtils.isEmpty(deliveryOrders)) {
            return null;
        }
        deliveryOrderComponent.addEleOrderDetailJson(deliveryOrders);
        // 增加配送记录
        addDeliveryOrderRecord(context, deliveryOrders);
        // 告诉用户饿单正在配送中
        try {
            notifyUserOrderIsDeliverying(context, deliveryOrders);
        } catch (Exception e) {
            logger.error("配送中短信通知用户失败", e);
        }
        // 告诉napos系统以上饿单在配送中
        try {
            deliveryOrderComponent.tellNaposEleOrderStatus(context, deliveryOrders, 2);
        } catch (Exception e) {
            logger.error("回调napos失败", e);
        }
        List<String> ids = deliveryOrderComponent.getDeilvieryOrderIDS(deliveryOrders);
        if (!CollectionUtils.isEmpty(ids)) {
            // deliveryOrderComponent.deleteWaitingToDeliveryOrderIdsInRedis(ids);
            // 再写入配送中的单号有哪些
            deliveryOrderComponent.writeDeliveryingOrderIdsToRedis(ids);
        }
        deliveryOrderComponent.addRstName(deliveryOrders);
        return deliveryOrders;

    }

    /**
     * 扫码创建配送单 扫码创建配送单已经没用了，所以后续没改了
     * 
     * @param context
     * @param eleOrderIds
     * @param actionType
     * @return
     * @throws UserException
     */
    // private List<DeliveryOrder> fetchDeliveryOrdersByBarCode(Context context, String[] eleOrderIds, String
    // actionType)
    // throws UserException {
    // // 判断是否已经存在本地了，这里的result里面可能有拉取到了本系统的配送单，但是配送状态为待配送的
    // Map<String, DeliveryOrder> result = getDeliveryOrdersByEleOrderIds(eleOrderIds);
    // Set<String> keys = new HashSet<String>();
    // // result里待取餐的配送单
    //
    // //int rst_id = stationRestaurantDao.getEleRestaurantIdByStaionId(context.getStationID());
    // List<String> waitToDeliveryIds = getWaitToDeliveryOrderIds(result, rst_id);
    //
    // if (!CollectionUtils.isEmpty(result)) {
    // keys = result.keySet();
    // }
    // // 剩下的这些需要去调用napos拿去饿单
    // Set<String> naposEleOrderIds = diff(keys, eleOrderIds);
    // // 判断配送员是否能对这些饿单进行操作
    // validateEleOrderIdCanBeChanged(context, naposEleOrderIds, DeliveryOrderContant.FETCH_ORDER_ACTION_TYPE_BARCODE);
    // // 传入进来的码已经全部被扫过
    // if (CollectionUtils.isEmpty(naposEleOrderIds) && CollectionUtils.isEmpty(waitToDeliveryIds)) {
    // throw new SystemException("DELIVERY_ORDER_ERROR_561", "该码不在您的餐厅下或者已经被扫描过了");
    // }
    // // 扫码去napos拉取的饿单信息
    // Collection<TEleOrder> eleOrders = getEleOrder(naposEleOrderIds).values();
    //
    // RedisLock redisLock = new RedisLock("DELIVERY_ORDER", String.valueOf(context.getStationID()), jedisPool);
    // redisLock.lock(3000, 2);
    // List<DeliveryOrder> deliveryOrders;
    // try {
    // deliveryOrders = deliveryOrderComponent.changeEleOrdersToDeliveryOrders(context, eleOrders, actionType,
    // DeliveryOrderContant.DELIVERYING);
    // } finally {
    // redisLock.unlock();
    // }
    // try {
    // // 在本地，但是待配送的配送单
    // List<DeliveryOrder> localDeliveryOrders = new ArrayList<DeliveryOrder>();
    // if (!CollectionUtils.isEmpty(waitToDeliveryIds)) {
    // for (String waitToDeliveryId : waitToDeliveryIds) {
    // localDeliveryOrders.add(result.get(waitToDeliveryId));
    // }
    // }
    // // 这里需要做六个小时时间验证，如果超过了六个小时，则remove掉
    // deliveryOrderComponent.filterDeliveryOrder(localDeliveryOrders);
    // if (!CollectionUtils.isEmpty(localDeliveryOrders)) {
    // for (DeliveryOrder deliveryOrder : localDeliveryOrders) {
    //
    // deliveryOrders.add(deliveryOrder);
    // // 重新开始计时，可以算某个配送员的配送时间
    // deliveryOrder.setCreated_at(new Timestamp(System.currentTimeMillis()));
    // deliveryOrderComponent.changeDeliveryOrderStatus(context, deliveryOrder,
    // DeliveryOrderContant.DELIVERYING, true, "");
    // }
    // }
    // return deliveryOrders;
    // } catch (Exception e) {
    // logger.error("系统异常", e);
    // throw new SystemException("DELIVERY_ORDER_ERROR_561", "系统异常");
    // }
    //
    // }

    /**
     * 批量勾选创建配送单。该场景下，饿单已经拉取过来了，且已经转为了配送单，配送单状态为代配送
     * 
     * @param context
     * @param eleOrderIds
     * @param actionType
     * @return
     * @throws UserException
     */
    private List<DeliveryOrder> fetchDeliveryOrdersList(Context context, String[] eleOrderIds, String actionType)
            throws UserException {
        // 此时只拉取本地的待配送的配送单信息。
        Set<String> keys = new HashSet<String>();
        keys.addAll(Arrays.asList(eleOrderIds));
        // 判断十二个小时内的单子是否超过了200单
        List<DeliveryOrder> deliveryingOrders = deliveryOrderDao.getDeliveryOrdersByFilter(
                DeliveryOrderContant.DELIVERYING, context.getUser().getId(), null, null, null, null);
        List<DeliveryOrder> exceptionOrders = deliveryOrderDao.getDeliveryOrdersByFilter(
                DeliveryOrderContant.EXCEPTION, context.getUser().getId(), null, null, null, null);
        int total = keys.size();
        if (!CollectionUtils.isEmpty(deliveryingOrders)) {
            total = total + deliveryingOrders.size();
        }
        if (!CollectionUtils.isEmpty(exceptionOrders)) {
            total = total + exceptionOrders.size();
        }
        if (total > 200) {
            throw new UserException("OREDER_COUNT_OUT_ERROR_100", "当前已拉取的订单已经超过200单，请稍后再试");
        }
        // 判断配送员是否能对这些饿单进行操作
        validateEleOrderIdCanBeChanged(context, keys, DeliveryOrderContant.FETCH_ORDER_ACTION_TYPE_LIST);
        if (CollectionUtils.isEmpty(keys)) {
            throw new SystemException("DELIVERY_ORDER_ERROR_561", "您选取的单子不存在，请刷新重试");
        }
        List<DeliveryOrder> deliveryOrders = new ArrayList<DeliveryOrder>();
        // 备注：以下代码为加了缓存锁的代码
        RedisLock redisLock = new RedisLock("station_id", String.valueOf(context.getStationID()), jedisPool);
        // 多个配送单处理2s超时时间肯定够了，该key值失效时间为3s。key失效时间必须比锁时间长。在finally会释放改锁
        redisLock.lock(2000, 3);
        try {
            Map<String, DeliveryOrder> result = getWaitToDeliveryDeliveryOrdersByEleOrderIds(keys);
            if (CollectionUtils.isEmpty(result)) {
                throw new UserException("DELIVERY_ORDER_ERROR_561", "用户操作异常");
            }
            for (String key : keys) {
                DeliveryOrder order = result.get(key);
                if (order == null) {
                    continue;
                }
                int rstIdPk = restaurantDao.getRestaurantByEleId(order.getRst_id()).getId();
                int stationId = stationRestaurantDao.getStationRestaurantByRstId(rstIdPk).getStation_id();
                order.setCreated_at(new Timestamp(System.currentTimeMillis()));
                order.setStation_id(stationId);
                order.setStatus(DeliveryOrderContant.DELIVERYING);
                order.setTaker_id(context.getUser().getId());
                order.setTaker_mobile(context.getUser().getMobile());
                deliveryOrders.add(result.get(key));
                updateDeliveryOrderStatus(stationId, DeliveryOrderContant.DELIVERYING, result.get(key).getId(), context
                        .getUser().getId(), context.getUser().getMobile());
            }
        } catch (Exception e) {
            if (!CollectionUtils.isEmpty(deliveryOrders)) {
                logger.error("批量勾选确认配送单部分成功部分失败", e);
            } else {
                throw new UserException("DELIVERY_ORDER_ERROR_561", "您的配送单已被您同事取走");
            }
        } finally {
            redisLock.unlock();
        }
        return deliveryOrders;
    }

    /**
     * 获取待配送的配送单信息
     * 
     * @param eleOrderIds
     * @return
     */
    private Map<String, DeliveryOrder> getWaitToDeliveryDeliveryOrdersByEleOrderIds(Set<String> eleOrderIds) {
        Map<String, DeliveryOrder> result = new HashMap<String, DeliveryOrder>();
        for (String eleOrderId : eleOrderIds) {
            DeliveryOrder deliveryOrder = deliveryOrderDao.getWaitToDeliveryDeliveryOrderByEleOrderId(Long
                    .valueOf(eleOrderId));
            if (deliveryOrder != null) {
                result.put(eleOrderId, deliveryOrder);
            }
        }
        return result;
    }

    /**
     * 根据配送单和人的信息，写入配送记录相关信息
     * 
     * @param context
     * @param deliveryOrders
     */
    private void addDeliveryOrderRecord(Context context, List<DeliveryOrder> deliveryOrders) {
        for (DeliveryOrder deliveryOrder : deliveryOrders) {
            addDeliveryOrderRecord(context, deliveryOrder);
        }
    }

    /**
     * 根据配送单和人的信息，写入配送记录相关信息
     *
     * @param context
     * @param deliveryOrder
     */
    private void addDeliveryOrderRecord(Context context, DeliveryOrder deliveryOrder) {
        DeliveryOrderRecord deliveryOrderRecord = new DeliveryOrderRecord();
        deliveryOrderRecord.setTaker_mobile(deliveryOrder.getTaker_mobile());
        deliveryOrderRecord.setCreated_at(new Timestamp(System.currentTimeMillis()));
        deliveryOrderRecord.setDelivery_order_id(deliveryOrder.getId());
        deliveryOrderRecord.setEle_order_id(deliveryOrder.getEle_order_id());
        // 这里需要定义操作ID
        deliveryOrderRecord.setoperation_id(DeliveryOrderContant.DELIVERYING);
        deliveryOrderRecord.setOperator_id(context.getUser().getId());
        deliveryOrderRecord.setStation_id(context.getUserStationRoles().get(0).getStation_id());
        deliveryOrderRecord.setStatus(DeliveryOrderContant.DELIVERYING);
        deliveryOrderRecord.setoperation_id(DeliveryOrderContant.OPERATION_DELIVERYING);
        deliveryOrderRecord.setTaker_id(context.getUser().getId());
        deliveryOrderRecord.setRemark("");
        deliveryOrderRecordDao.addDeliveryOrderRecord(deliveryOrderRecord);

    }

    public void addDeliveryOrder(Context context, List<DeliveryOrder> deliveryOrders) {
        for (DeliveryOrder deliveryOrder : deliveryOrders) {
            deliveryOrderDao.insert(deliveryOrder);
        }
    }

    /**
     * 调用短信接口，告诉用户饿单正在配送中
     * 
     * @param context
     * @param deliveryOrders
     * @throws UserException
     */
    private void notifyUserOrderIsDeliverying(Context context, List<DeliveryOrder> deliveryOrders) throws UserException {
        String mapHref = null;
        int result = 0;
        try {
            String response = httpRequest.doRequest(locationUrl + context.getUser().getId(), null, null, "utf-8")
                    .getResponseContent();
            AddressResponse responseEntity = SerializeUtil.jsonToBean(response, AddressResponse.class);
            if ("200".equals(responseEntity.getErr_code())) {
                result = responseEntity.getData();
            }
        } catch (Exception e) {
            logger.error("调用地图信息失败", e);
        }
        for (DeliveryOrder deliveryOrder : deliveryOrders) {
            // 判断是否有地图相关的信息
            if (result == 0) {
                mapHref = null;
            } else {
                mapHref = mapUrl + deliveryOrder.getUuid();
            }
            if (mapHref != null) {
                Map<String, Object> postData = new HashMap<String, Object>();
                postData.put("url", mapHref);
                postData.put("access_type", "web");
                CompressUrlResponse compressUrlResponse = new CompressUrlResponse();
                try {
                    compressUrlResponse = SerializeUtil.jsonToBean(
                            new HttpRequest().doRequest("http://dwz.cn/create.php", postData, null, "utf-8")
                                    .getResponseContent(), CompressUrlResponse.class);
                } catch (Exception e) {
                    logger.error("生成短连接失败", e);
                }
                // 短连接生成成功
                if (compressUrlResponse.getStatus() == 0) {
                    mapHref = compressUrlResponse.getTinyurl();
                }
            }
            logger.info("发送给用户的短信里包含链接{}", mapHref);
            if (StringUtils.isEmpty(mapHref)) {
                coffeeHermesService.sendDeliveringMessage(String.valueOf(deliveryOrder.getReceiver_mobile()),
                        String.valueOf(context.getUser().getMobile()), context.getUser().getName());
            } else {
                coffeeHermesService.sendDeliveringMessage(String.valueOf(deliveryOrder.getReceiver_mobile()),
                        String.valueOf(context.getUser().getMobile()), context.getUser().getName(), mapHref);
            }
        }
    }

    /**
     * 根据额单号获取本地的配送单信息
     * 
     * @param eleOrderIds
     * @return
     */
    private Map<String, DeliveryOrder> getDeliveryOrdersByEleOrderIds(String[] eleOrderIds) {
        Map<String, DeliveryOrder> result = new HashMap<String, DeliveryOrder>();
        for (String eleOrderId : eleOrderIds) {
            DeliveryOrder deliveryOrder = deliveryOrderDao.getDeliveryOrderByEleOrderId(Long.valueOf(eleOrderId));
            if (deliveryOrder != null) {
                result.put(eleOrderId, deliveryOrder);
            }
        }
        return result;
    }

    /**
     * 判断配送员是否能对这些饿单进行操作，返回的集合是能操作的饿单ID的集合
     * 
     * @param context
     * @param eleOrderIds
     * @return
     * @throws UserException
     */
    private void validateEleOrderIdCanBeChanged(Context context, Set<String> eleOrderIds, String createTpye)
            throws UserException {

        Set<Integer> rstIds = new HashSet<Integer>();
        rstIds = commonDeliveryOrderService.getRstIdsByContext(context);
        List<DeliveryOrder> deliveryOrders = new ArrayList<DeliveryOrder>();
        // 如果是批量选择则只用查询本地库，如果是扫码拉取，则需要调用接口
        if (!DeliveryOrderContant.FETCH_ORDER_ACTION_TYPE_BARCODE.equals(createTpye)) {
            Iterator<String> iterator = eleOrderIds.iterator();
            while (iterator.hasNext()) {
                String eleOrderId = iterator.next();
                DeliveryOrder deliveryOrder = deliveryOrderDao.getDeliveryOrderByEleOrderId(Long.valueOf(eleOrderId));
                if (deliveryOrder == null || !rstIds.contains(deliveryOrder.getRst_id())) {
                    iterator.remove();
                    continue;
                }
                deliveryOrders.add(deliveryOrder);
            }
            // 再做一个几个小时的校验
            deliveryOrderComponent.filterDeliveryOrder(deliveryOrders);
            eleOrderIds.clear();
            if (CollectionUtils.isEmpty(deliveryOrders)) {
                return;
            }
            for (DeliveryOrder deliveryOrder : deliveryOrders) {
                eleOrderIds.add(String.valueOf(deliveryOrder.getEle_order_id()));
            }
            return;
        }
        // // 如果是扫码
        // // 调用接口根据额单号获取饿单信息
        // Collection<TEleOrder> eleOrders = getEleOrder(eleOrderIds).values();
        // // 判断这些饿单是否属于配送员所在站点
        // for (TEleOrder tEleOrder : eleOrders) {
        // if (rst_id != tEleOrder.getRestaurantId()) {
        // eleOrderIds.remove(String.valueOf(tEleOrder.getId()));
        // }
        // }

    }

    private List<String> getWaitToDeliveryOrderIds(Map<String, DeliveryOrder> map, int rst_id) {
        if (CollectionUtils.isEmpty(map)) {
            return null;
        }
        List<String> waitToDeliveryIds = new ArrayList<String>();
        Iterator<Entry<String, DeliveryOrder>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, DeliveryOrder> entry = iterator.next();
            if (entry.getValue().getStatus() == DeliveryOrderContant.WAITTO_DELIVERY
                    && entry.getValue().getRst_id() == rst_id) {
                waitToDeliveryIds.add(entry.getKey());
            }
        }
        return waitToDeliveryIds;

    }

    /**
     * 判断string[]数组里的是否在set数组里面，返回的是ids里面不在set里面的元素
     * 
     * @param set
     * @param ids
     * @return
     */
    private Set<String> diff(Set<String> set, String[] ids) {
        Set<String> result = new HashSet<String>();

        if (CollectionUtils.isEmpty(set)) {
            for (String id : ids) {
                result.add(id);
            }
        }
        for (String id : ids) {
            if (!set.contains(id)) {
                result.add(id);
            }
        }
        return result;

    }

    /**
     * 更新配送单为配送中：注意，该方法会修改配送单的创建时间，挂在一个配送员身上，然后可以算配送时长
     * 
     * @param status
     * @param deliveryId
     * @param userId
     * @param mobile
     * @return
     */
    @Override
    public Integer updateDeliveryOrderStatus(int stationId, int status, long deliveryId, int userId, long mobile) {
        int result = 0;
        try {
            result = deliveryOrderDao.updateDeliveryOrderStatus(stationId, userId, mobile, status, deliveryId);
        } catch (Exception e) {
            logger.error("更新配送单状态失败", e);
            throw ExceptionFactory.newSystemException(ExceptionCode.DELIVERY_ORDER_STATUS_UPDATE_FAIL);

        }
        return result;
    }

    /**
     * 根据饿单号去napos拉取饿单
     * 
     * @param set
     * @return
     * @throws UserException
     * @throws NumberFormatException
     */
    private Map<String, TEleOrder> getEleOrder(Set<String> set) throws NumberFormatException, UserException {
        Map<String, TEleOrder> result = new HashMap<String, TEleOrder>();
        for (String s : set) {
            TEleOrder tEleOrder = talarisNaposService.getEleOrderByEleOrderId(Long.valueOf(s));
            result.put(s, tEleOrder);
        }
        return result;

    }

    @CacheLock(lockedPrefix = "OPRERATOR_RECEIVER_MOBILE")
    public DeliveryOrder createDeliveryOrder(Context context, DeliveryOrder inOrder, @LockedObject Long mobile)
            throws UserException {
        List<DeliveryOrder> deliveryOrders = new ArrayList<>();
        deliveryOrders.add(inOrder);

        DeliveryOrder orderDelivering = deliveryOrderDao.getDeliveryOrder(inOrder.getReceiver_mobile(),
                DeliveryOrderContant.SOURCE_FROM_MANAULLY, DeliveryOrderContant.DELIVERYING);
        if (orderDelivering != null) {
            throw ExceptionFactory.newUserException(ExceptionCode.DELIVERY_ORDER_ERROR_501);
        }
        Restaurant rst = restaurantDao.getRestaurantByEleId(inOrder.getRst_id());

        if (rst == null) {
            throw ExceptionFactory.newUserException(ExceptionCode.WALLE_POST_ERROR_002);
        }

        Station station = stationDao.getStationByRestaurantId(rst.getId());
        if (station == null) {
            throw ExceptionFactory.newUserException(ExceptionCode.STATION_ERROR_008);
        }

        if (inOrder.getRst_id() != 0) {
            inOrder.setRst_name(rst.getName());
            inOrder.setStation_id(station.getId());
        }
        deliveryOrderDao.addDeliveryOrder(inOrder);
        DeliveryOrder deliveryOrder = deliveryOrderDao.getDeliveryOrderByEleOrderId(inOrder.getEle_order_id());
        addDeliveryOrderRecord(context, inOrder);
        inOrder.setId(deliveryOrder.getId());
        List<String> ids = deliveryOrderComponent.getDeilvieryOrderIDS(deliveryOrders);
        if (!CollectionUtils.isEmpty(ids)) {
            deliveryOrderComponent.writeDeliveryingOrderIdsToRedis(ids);
        }

        // 告诉用户饿单正在配送中
        // TODO: we need a new msg template
        try {
            if (getDeliveryOrderService.needManuallyOrderCustomerNotifiedThroughMsg(inOrder.getReceiver_mobile())) {
                logger.info("已经为该客户[" + inOrder.getReceiver_mobile() + "]配送超过两次以上，发送短信通知用户订单正在配送中.");
                notifyUserOrderIsDeliverying(context, deliveryOrders);
            } else {
                logger.info("已经为该客户[" + inOrder.getReceiver_mobile() + "]配送不到两次，将不发送短信通知用户订单已配送.");
            }
        } catch (Exception e) {
            logger.error("配送中短信通知用户失败", e);
        }

        return inOrder;
    }

    public Station getStationByRestaurantId(int rstId) {
        return stationDao.getStationByRestaurantId(rstId);
    }
}
