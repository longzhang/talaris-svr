package me.ele.talaris.service.deliveryorder.impl.state;

import me.ele.talaris.deliveryorder.dto.DeliveryOrder;
import me.ele.talaris.exception.UserException;
import me.ele.talaris.model.Context;
import me.ele.talaris.service.deliveryorder.IConfirmDeliverdService;

import java.util.List;
import java.util.Map;

/**
 * Created by Daniel on 15/5/5.
 */
public class ConfirmReceiptState extends DeliveryOrderState {
    private IConfirmDeliverdService confirmDeliverdService;

    @Override
    public void create(Context context, String[] eleOrderIds, String actionType) throws UserException {

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
        return null;

    }

    @Override
    public Map<Long, Integer> confirmReceipt(Context context,
                               String[] deliveryOrderIds) throws UserException {
        return confirmDeliverdService.confirmDelivered(context, deliveryOrderIds);
    }

    @Override
    public DeliveryOrder markAbnormal(Context context, String deliveryOrderId,
                             String remark) throws UserException {
        return null;
    }

    @Override
    public Map<Long, DeliveryOrder> markNormal(Context context,
                           String[] deliveryOrderIds) throws UserException {
        return null;
    }

}
