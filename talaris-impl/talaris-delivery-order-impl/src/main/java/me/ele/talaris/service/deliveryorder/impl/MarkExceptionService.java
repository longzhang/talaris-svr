package me.ele.talaris.service.deliveryorder.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import me.ele.talaris.constant.DeliveryOrderContant;
import me.ele.talaris.deliveryorder.dto.DeliveryOrder;
import me.ele.talaris.deliveryorder.persistent.dao.DeliveryOrderDao;
import me.ele.talaris.exception.SystemException;
import me.ele.talaris.exception.UserException;
import me.ele.talaris.model.Context;
import me.ele.talaris.redis.CacheLock;
import me.ele.talaris.redis.LockedObject;
import me.ele.talaris.redis.RedisLock;
import me.ele.talaris.service.deliveryorder.IMarkExceptionService;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.JedisPool;

/**
 * 标记异常
 * 
 * @author zhengwen
 *
 */
@Service
public class MarkExceptionService implements IMarkExceptionService {
    private final static Logger logger = LoggerFactory.getLogger(MarkExceptionService.class);

    @Autowired
    DeliveryOrderDao deliveryOrderDao;
    @Autowired
    DeliveryOrderComponent deliveryOrderComponent;
    @Autowired
    CommonDeliveryOrderService commonDeliveryOrderService;
    @Autowired
    JedisPool jedisPool;

    /**
     * 标记为异常,返回的是异常配送单的信息
     * 
     * @param context
     * @param deliveryOrderId
     * @param remark
     * @return
     * @throws UserException
     */
    @Override
    @CacheLock(lockedPrefix = "OPRERATOR_ORDER_ID", expireTime = 4, timeOut = 3000)
    public DeliveryOrder markException(Context context, @LockedObject String deliveryOrderId, String remark)
            throws UserException {
        if (StringUtils.isEmpty(deliveryOrderId)) {
            return null;
        }
        DeliveryOrder deliveryOrder = deliveryOrderDao.getDeliveryOrdersById(Long.valueOf(deliveryOrderId));
        if (deliveryOrder == null) {
            return null;
        }
        if (deliveryOrder.getStatus() == DeliveryOrderContant.DELIVERIED) {
            List<String> ids = new ArrayList<String>();
            ids.add(String.valueOf(deliveryOrder.getId()));
            throw new SystemException("TTL_ERROR_100", "该配送单在您手中已经超过了"
                    + deliveryOrderComponent.getMarkConfirmDeliveried() + "个小时,自动标记为已经送达");
        }

        // 因为现在有个定时任务自动扫描配送中的订单，如果超过了三个小时则自动标记为异常了，所以现在不用校验是否超时了
        // deliveryOrderComponent.validateDeliveryOrder(context, deliveryOrders);
        try {

            // 获取不到配送单信息，或者是已经是异常状态了、或者是已经送达了则返回null
            if ((DeliveryOrderContant.EXCEPTION == deliveryOrder.getStatus())
                    || context.getUser().getId() != deliveryOrder.getTaker_id()) {

                return null;
            }
            Set<Integer> rstIDS = commonDeliveryOrderService.getRstIdsByContext(context);
            if (!rstIDS.contains(deliveryOrder.getRst_id())) {
                throw new UserException("REMOVE_ERROR_100", "您无权限");
            }
            List<DeliveryOrder> deliveryOrders = new ArrayList<DeliveryOrder>();
            deliveryOrders.add(deliveryOrder);
            deliveryOrderComponent.changeDeliveryOrderStatus(context, deliveryOrder, DeliveryOrderContant.EXCEPTION,
                    false, remark);
            deliveryOrder.setException_remark(remark);
            List<String> ids = new ArrayList<String>();
            ids.add(deliveryOrderId);
            deliveryOrderComponent.deleteDeliveryingOrderIdsInRedis(ids);
            deliveryOrderComponent.addRstName(deliveryOrders);
            return deliveryOrder;
        } catch (SystemException e) {
            logger.error("更新配送单状态失败", e);
            throw new SystemException("DELIVERY_ORDER_ERROR_590", "系统异常，请重试");
        }
    }
}
