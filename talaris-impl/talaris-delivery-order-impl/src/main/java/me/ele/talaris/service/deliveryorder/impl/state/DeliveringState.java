package me.ele.talaris.service.deliveryorder.impl.state;

import me.ele.talaris.deliveryorder.dto.DeliveryOrder;
import me.ele.talaris.exception.UserException;
import me.ele.talaris.model.Context;
import me.ele.talaris.service.deliveryorder.ICreateDeliveryOrderService;
import me.ele.talaris.service.deliveryorder.IMarkNormalService;

import java.util.List;
import java.util.Map;

/**
 * Created by Daniel on 15/5/5.
 */
public class DeliveringState extends DeliveryOrderState {
    private ICreateDeliveryOrderService createDeliveryOrderService;
    private IMarkNormalService markNormalService;

    @Override
    public void create(Context context,
                       String[] eleOrderIds, String actionType)  throws UserException {

    }

    @Override
    public void assign() {

    }

    @Override
    public void accept() {

    }

    @Override
    public List<DeliveryOrder> delivery(Context context,
                         String[] eleOrderIds, String actionType) throws UserException{
        return createDeliveryOrderService.fetchDeliveryOrders(context, eleOrderIds, actionType);
    }

    @Override
    public Map<Long, Integer> confirmReceipt(Context context, String[] deliveryOrderIds) throws UserException {
        this.deliveryOrderContext.setOrderState(DeliveryOrderContext.confirmReceiptState);
        return this.deliveryOrderContext.getOrderState().confirmReceipt(context, deliveryOrderIds);
    }

    @Override
    public DeliveryOrder markAbnormal(Context context, String deliveryOrderId,
                             String remark) throws UserException {
        this.deliveryOrderContext.setOrderState(DeliveryOrderContext.abnormalState);
        return this.deliveryOrderContext.getOrderState().markAbnormal(context, deliveryOrderId, remark);
    }

    @Override
    public Map<Long, DeliveryOrder> markNormal(Context context,
                           String[] deliveryOrderIds) throws UserException{
        return markNormalService.markNormal(context, deliveryOrderIds);
    }

}
