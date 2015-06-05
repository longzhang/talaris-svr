package me.ele.talaris.service.deliveryorder.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.ele.talaris.dao.EleOrderDetailDao;
import me.ele.talaris.dao.StationRestaurantDao;
import me.ele.talaris.deliveryorder.dto.DeliveryOrder;
import me.ele.talaris.deliveryorder.dto.DeliveryOrderEx;
import me.ele.talaris.deliveryorder.dto.PartDeliveryOrder;
import me.ele.talaris.deliveryorder.persistent.dao.DeliveryOrderDao;
import me.ele.talaris.exception.UserException;
import me.ele.talaris.model.Context;
import me.ele.talaris.napos.service.INaposService;
import me.ele.talaris.redis.RedisClient;
import me.ele.talaris.service.deliveryorder.IGetWaitToDeliveryOrderService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import redis.clients.jedis.JedisPool;

/**
 * 获取待配送的配送单
 * 
 * @author zhengwen
 *
 */
@Service
public class GetWaitToDeliveryOrderService implements IGetWaitToDeliveryOrderService {
    private final static Logger logger = LoggerFactory.getLogger(GetWaitToDeliveryOrderService.class);
    @Autowired
    StationRestaurantDao stationRestaurantDao;
    @Autowired
    INaposService talarisNaposService;
    @Autowired
    DeliveryOrderDao deliveryOrderDao;
    @Autowired
    EleOrderDetailDao eleOrderDetailDao;
    @Autowired
    DeliveryOrderComponent deliveryOrderComponent;
    @Autowired
    JedisPool jedisPool;
    @Autowired
    RedisClient redisClient;

    @Autowired
    CommonDeliveryOrderService commonDeliveryOrderService;

    /**
     * 配送员查看自己所在站点对应的配送单，这里的配送单拉取出来后全部是待配送的
     * 
     * @param context
     * @return
     * @throws UserException
     */
    @Override
    public List<DeliveryOrder> getDeliveryOrdersByContext(Context context, int rst_id) throws UserException {
        Set<Integer> restaurantIds = new HashSet<Integer>();
        restaurantIds = commonDeliveryOrderService.getRstIdsByContext(context);
        if (!restaurantIds.contains(rst_id)) {
            throw new UserException("DELIVERY_ORDER_ERROR_561", "您没有权限");
        }
        // 这里加上配送单创建时间的限制，加上两天时间
        List<DeliveryOrder> allDeliveryOrders = deliveryOrderDao.getRstWaiToDeliveryOrder(rst_id);

        // List<TEleOrder> tEleOrders = talarisNaposService.getEleOrderListByRestaurantId(rst_id);
        // RedisLock redisLock = new RedisLock("DELIVERY_ORDER", String.valueOf(context.getStationID()), jedisPool);
        // redisLock.lock(3000, 2);
        // try {
        // deliveryOrderComponent.changeEleOrdersToDeliveryOrders(context, tEleOrders,
        // DeliveryOrderContant.FETCH_ORDER_ACTION_TYPE_LIST, DeliveryOrderContant.WAITTO_DELIVERY);
        // } catch (Exception e) {
        // logger.error("转化饿单到配送单的时候失败", e);
        // throw new SystemException("DELIVERY_ORDER_ERROR_561", "获取配送单失败");
        // } finally {
        // redisLock.unlock();
        // }
        // 增加一个细节：就是已经变成配送中的配送单，然后取消了，这个时候没有给napos反馈，这个时候存在本地库里的是代配送的订单，而这个时候napos是没有的，所以需要再查询一次本地
        // 2015-03-09 17:39:38
        // List<DeliveryOrder> allDeliveryOrders = deliveryOrderDao.getWaitToDeliveryDeliveryOrderByStationId(context
        // .getStationID());
        // 在时间上做过滤。里面分为预订单和普通订单
        deliveryOrderComponent.filterDeliveryOrder(allDeliveryOrders);

        // 将待配送的单子基本信息写入redis
        // List<String> ids = deliveryOrderComponent.getDeilvieryOrderIDS(allDeliveryOrders);
        // if (!CollectionUtils.isEmpty(ids)) {
        // deliveryOrderComponent.writeWaitingToDeliveryOrderIdsToRedis(ids);
        // }

        return allDeliveryOrders;
    }

    @Override
    public List<PartDeliveryOrder> changeLongDeliveryOrderToSort(List<DeliveryOrderEx> deliveryOrderExs) {
        if (CollectionUtils.isEmpty(deliveryOrderExs)) {
            return null;
        }
        List<PartDeliveryOrder> partDeliveryOrders = new ArrayList<PartDeliveryOrder>();
        for (DeliveryOrderEx deliveryOrderEx : deliveryOrderExs) {
            PartDeliveryOrder partDeliveryOrder = new PartDeliveryOrder();
            partDeliveryOrder.setEle_order_id(deliveryOrderEx.getEle_order_id());
            partDeliveryOrder.setEle_order_sn(deliveryOrderEx.getEle_order_sn());
            if (deliveryOrderEx.getBooked_time() == null) {
                partDeliveryOrder.setIs_booked(0);
            } else {
                partDeliveryOrder.setIs_booked(1);
                partDeliveryOrder.setBooked_time(deliveryOrderEx.getBooked_time());
            }
            partDeliveryOrders.add(partDeliveryOrder);
        }
        return partDeliveryOrders;

    }

}
