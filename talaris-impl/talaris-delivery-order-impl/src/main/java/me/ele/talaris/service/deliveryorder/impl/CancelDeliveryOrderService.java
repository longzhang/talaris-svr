package me.ele.talaris.service.deliveryorder.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.ele.talaris.constant.DeliveryOrderContant;
import me.ele.talaris.deliveryorder.dto.DeliveryOrder;
import me.ele.talaris.deliveryorder.persistent.dao.DeliveryOrderDao;
import me.ele.talaris.exception.SystemException;
import me.ele.talaris.exception.UserException;
import me.ele.talaris.model.Context;
import me.ele.talaris.redis.CacheLock;
import me.ele.talaris.redis.LockedObject;
import me.ele.talaris.redis.RedisClient;
import me.ele.talaris.redis.RedisLock;
import me.ele.talaris.service.deliveryorder.ICancelDeliveryOrderService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import redis.clients.jedis.JedisPool;

/**
 * 
 * @author zhengwen
 * 取消配送
 */
@Service
public class CancelDeliveryOrderService implements ICancelDeliveryOrderService {
    private final static Logger logger = LoggerFactory.getLogger(CancelDeliveryOrderService.class);

    @Autowired
    DeliveryOrderDao deliveryOrderDao;
    @Autowired
    DeliveryOrderComponent deliveryOrderComponent;
    @Autowired
    RedisClient redisClient;
    @Autowired
    CommonDeliveryOrderService commonDeliveryOrderService;
    @Autowired
    JedisPool jedisPool;

    @Override
    @CacheLock(lockedPrefix = "OPRERATOR_ORDER_ID", expireTime = 4, timeOut = 3000)
    public Map<String, Integer> cancel(Context context, @LockedObject String deliveryOrderId) throws UserException {
        Map<String, Integer> map = new HashMap<String, Integer>();
        if (StringUtils.isEmpty(deliveryOrderId)) {
            return null;
        }
        // 这里可能抛出异常，不过不影响DB数据，直接让其抛出，最后被拦截过系统异常
        try {
            DeliveryOrder deliveryOrder = deliveryOrderDao.getDeliveryOrdersById(Long.valueOf(deliveryOrderId));
            // 做些基本校验
            if (!validateCanCancel(context, deliveryOrder)) {
                return null;
            }
            if (deliveryOrder.getSource() != DeliveryOrderContant.SOURCE_FROM_ELEME) {
                deliveryOrderComponent.changeDeliveryOrderStatus(context, deliveryOrder,
                        DeliveryOrderContant.NOT_ELE_ORDER_CANCELED, false, "");
            }
            // 开始操作DB
            else {
                deliveryOrderComponent.changeDeliveryOrderStatus(context, deliveryOrder,
                        DeliveryOrderContant.WAITTO_DELIVERY, false, "");
            }
            map.put(deliveryOrderId, 1);
            List<String> ids = new ArrayList<String>();
            ids.add(deliveryOrderId);
            deliveryOrderComponent.deleteDeliveryingOrderIdsInRedis(ids);
            return map;
        } catch (UserException e) {
            logger.error("取消配送单失败", e);
            throw new UserException("APP_ERROR_553", "取消配送单配送失败");
        }

    }

    private boolean validateCanCancel(Context context, DeliveryOrder deliveryOrder) throws UserException {
        // 因为现在有个定时任务自动扫描配送中的订单，如果超过了三个小时则自动标记为异常了，所以现在不用校验是否超时了

        // if (deliveryOrder != null) {
        // List<DeliveryOrder> deliveryOrders = new ArrayList<DeliveryOrder>();
        // deliveryOrders.add(deliveryOrder);
        // // deliveryOrderComponent.validateDeliveryOrder(context, deliveryOrders);
        // }
        // 如果是空，或者状态为待配送，配送完成了，或者是异常订单，则不让取消
        if (deliveryOrder.getStatus() == DeliveryOrderContant.WAITTO_DELIVERY
                || deliveryOrder.getStatus() == DeliveryOrderContant.EXCEPTION) {
            return false;
        }
        if (deliveryOrder.getStatus() == DeliveryOrderContant.DELIVERIED) {
            List<String> ids = new ArrayList<String>();
            ids.add(String.valueOf(deliveryOrder.getId()));
            throw new SystemException("TTL_ERROR_100", "该配送单在您手中已经超过了"
                    + deliveryOrderComponent.getMarkConfirmDeliveried() + "个小时,自动标记为已经送达", ids);
        }

        // 不是自己的配送单则不让取消
        if (context.getUser().getId() != deliveryOrder.getTaker_id()) {
            return false;
        }
        Set<Integer> rstIDS = commonDeliveryOrderService.getRstIdsByContext(context);
        if (!rstIDS.contains(deliveryOrder.getRst_id())) {
            throw new UserException("REMOVE_ERROR_100", "您无权限");
        }
        return true;
    }

}
