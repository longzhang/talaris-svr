package me.ele.talaris.service.deliveryorder.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.ele.talaris.constant.DeliveryOrderContant;
import me.ele.talaris.deliveryorder.dto.DeliveryOrder;
import me.ele.talaris.deliveryorder.persistent.dao.DeliveryOrderDao;
import me.ele.talaris.exception.SystemException;
import me.ele.talaris.exception.UserException;
import me.ele.talaris.hermes.service.IHermesService;
import me.ele.talaris.model.Context;
import me.ele.talaris.service.deliveryorder.INotifyUserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 通知用户
 * 
 * @author zhengwen
 *
 */
@Service
public class NotifyUserService implements INotifyUserService {
    private final static Logger logger = LoggerFactory.getLogger(NotifyUserService.class);

    @Autowired
    IHermesService coffeeHermesService;
    @Autowired
    DeliveryOrderDao deliveryOrderDao;
    @Autowired
    DeliveryOrderComponent deliveryOrderComponent;
    @Value("${msg.voice.notify.template}")
    private String msgTemplate;

    @Autowired
    CommonDeliveryOrderService commonDeliveryOrderService;

    /**
     * 电话通知用户外卖已经送达，返回map的key为配送单ID，value为电话事件ID
     * 
     * @param context
     * @param deliveryOrderIds
     * @return
     * @throws UserException
     */
    @Override
    public Map<Long, Long> notifyCustomer(Context context, String[] deliveryOrderIds) throws UserException {
        if (deliveryOrderIds == null || deliveryOrderIds.length == 0) {
            return null;
        }
        List<String> stringDeliveryOrderIds = Arrays.asList(deliveryOrderIds);
        List<Long> longDeliveryOrderIds = new ArrayList<Long>();
        for (String s : stringDeliveryOrderIds) {
            longDeliveryOrderIds.add(Long.valueOf(s));
        }
        Map<Long, Long> map = new HashMap<Long, Long>();
        List<DeliveryOrder> deliveryOrders = deliveryOrderDao.getDeliveryOrdersByIdList(longDeliveryOrderIds);
        for (DeliveryOrder deliveryOrder : deliveryOrders) {
            if (deliveryOrder.getStatus() == DeliveryOrderContant.DELIVERIED) {
                List<String> ids = new ArrayList<String>();
                ids.add(String.valueOf(deliveryOrder.getId()));
                throw new SystemException("TTL_ERROR_100", "该配送单在您手中已经超过了"
                        + deliveryOrderComponent.getMarkConfirmDeliveried() + "个小时,自动标记为已经送达");
            }
        }
        // 因为现在有个定时任务自动扫描配送中的订单，如果超过了三个小时则自动标记为异常了，所以现在不用校验是否超时了
        // deliveryOrderComponent.validateDeliveryOrder(context, deliveryOrders);
        try {
            for (DeliveryOrder deliveryOrder : deliveryOrders) {

                if (deliveryOrder == null || deliveryOrder.getTaker_id() != context.getUser().getId()) {
                    map.put(Long.valueOf(deliveryOrder.getId()), 0L);
                    continue;
                }
                Set<Integer> rstIDS = commonDeliveryOrderService.getRstIdsByContext(context);
                if (!rstIDS.contains(deliveryOrder.getRst_id())) {
                    throw new UserException("REMOVE_ERROR_100", "您无权限");
                }
                long taskId = coffeeHermesService.sendAudioMessage(String.valueOf(deliveryOrder.getReceiver_mobile()),
                        msgTemplate, deliveryOrder.getId());
                coffeeHermesService.sendDeliveredMessage(String.valueOf(deliveryOrder.getReceiver_mobile()),
                        String.valueOf(deliveryOrder.getTaker_mobile()), context.getUser().getName());

                map.put(Long.valueOf(deliveryOrder.getId()), taskId);

            }
        } catch (Exception e) {
            logger.error("一键通知用户失败", e);
            throw new SystemException("DELIVERY_ORDER_ERROR_590", "系统异常，请重试");
        }
        return map;
    }

}
