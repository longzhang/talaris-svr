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
import me.ele.talaris.model.Context;
import me.ele.talaris.service.deliveryorder.IConfirmDeliverdService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * 确认送达
 * 
 * @author zhengwen
 *
 */
@Service
public class ConfirmDeliveredService implements IConfirmDeliverdService {
    private final static Logger logger = LoggerFactory.getLogger(ConfirmDeliveredService.class);

    @Autowired
    DeliveryOrderDao deliveryOrderDao;
    @Autowired
    DeliveryOrderComponent deliveryOrderComponent;
    @Autowired
    CommonDeliveryOrderService commonDeliveryOrderService;

    /**
     * 确认送达，返回的是map。单号对应是否成功，0对应失败，1对应成功
     * 
     * @param context
     * @param deliveryOrderIds
     * @return
     * @throws UserException
     */
    @Override
    public Map<Long, Integer> confirmDelivered(Context context, String[] deliveryOrderIds) throws UserException {
        if (deliveryOrderIds == null || deliveryOrderIds.length == 0) {
            return null;
        }
        List<String> stringDeliveryOrderIds = Arrays.asList(deliveryOrderIds);
        List<Long> longDeliveryOrderIds = new ArrayList<Long>();
        for (String s : stringDeliveryOrderIds) {
            longDeliveryOrderIds.add(Long.valueOf(s));
        }
        Map<Long, Integer> map = new HashMap<Long, Integer>();
        List<DeliveryOrder> deliveryOrders = deliveryOrderDao.getDeliveryOrdersByIdList(longDeliveryOrderIds);
        // 因为现在有个定时任务自动扫描配送中的订单，如果超过了三个小时则自动标记为异常了，所以现在不用校验是否超时了
        // deliveryOrderComponent.validateDeliveryOrder(context, deliveryOrders);
        if (CollectionUtils.isEmpty(deliveryOrders)) {
            return null;
        }
        try {
            for (DeliveryOrder deliveryOrder : deliveryOrders) {
                if (deliveryOrder == null || deliveryOrder.getTaker_id() != context.getUser().getId()
                        || deliveryOrder.getStatus() == DeliveryOrderContant.WAITTO_DELIVERY
                        || deliveryOrder.getStatus() == DeliveryOrderContant.EXCEPTION) {
                    map.put(Long.valueOf(deliveryOrder.getId()), DeliveryOrderContant.CONFIRM_DELIVEIED_FAIL);
                    continue;
                }
                Set<Integer> rstIDS = commonDeliveryOrderService.getRstIdsByContext(context);
                if (!rstIDS.contains(deliveryOrder.getRst_id())) {
                    throw new UserException("REMOVE_ERROR_100", "您无权限");
                }
                deliveryOrderComponent.changeDeliveryOrderStatus(context, deliveryOrder,
                        DeliveryOrderContant.DELIVERIED, true, "");
                map.put(Long.valueOf(deliveryOrder.getId()), DeliveryOrderContant.CONFIRM_DELIVEIED_SUCCESS);
                List<String> ids = new ArrayList<String>();
                ids.add(String.valueOf(deliveryOrder.getId()));
                deliveryOrderComponent.deleteDeliveryingOrderIdsInRedis(ids);
            }
        } catch (Exception e) {
            logger.error("确认送达失败", e);
            throw new SystemException("DELIVERY_ORDER_ERROR_590", "系统异常，请重试");
        }
        return map;
    }
}
