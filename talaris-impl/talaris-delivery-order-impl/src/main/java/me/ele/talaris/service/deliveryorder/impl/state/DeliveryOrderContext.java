package me.ele.talaris.service.deliveryorder.impl.state;

import me.ele.talaris.deliveryorder.dto.DeliveryOrder;
import me.ele.talaris.exception.UserException;
import me.ele.talaris.model.Context;
import me.ele.talaris.service.deliveryorder.IDeliveryOrderContextService;

import java.util.List;
import java.util.Map;

/**
 * Created by Daniel on 15/5/5.
 */
public class DeliveryOrderContext implements IDeliveryOrderContextService{
    /**
     * From now on(talaris 1.0.*) we used preState, deliveringState, confirmReceiptState and abnormalState only.
     * The relationship between state and operation as following diagram.
     *
     * [preState]           [deliveringState]                   [confirmReceiptState]
     *            --------                     -----------------
     *  -------> | Create | --------------->  | Confirm Receipt |  ------->
     *            --------   |        |        -----------------
     *                       |        |
     *             ---------------    |
     *            | Mark Abnormal |   |
     *             ---------------    |
     *                       |        |
     *       [abnormalState] |        |
     *              -------------     |
     *             | Mark Normal |  --
     *              -------------
     */

    public static DeliveryOrderState preState;
    public static DeliveryOrderState idleState;
    public static DeliveryOrderState unacceptedState;
    public static DeliveryOrderState acceptedState;
    public static DeliveryOrderState deliveringState;
    public static DeliveryOrderState confirmReceiptState;
    public static DeliveryOrderState abnormalState;


    public DeliveryOrderContext(){
        preState = new PreState();
        idleState = new IdleState();
        unacceptedState = new UnacceptedState();
        acceptedState = new AcceptedState();
        deliveringState = new DeliveringState();
        confirmReceiptState = new ConfirmReceiptState();
        abnormalState = new AbnormalState();
    }

    private DeliveryOrderState orderState;

    public DeliveryOrderState getOrderState(){
        return this.orderState;
    }

    public void setOrderState(DeliveryOrderState orderState){
        this.orderState = orderState;
        this.orderState.setDeliveryOrderContext(this);
    }

    @Override
    public void create(Context context,
                       String[] eleOrderIds, String actionType)  throws UserException {
        this.orderState.create(context, eleOrderIds, actionType);
    }

    @Override
    public void assign(){
        this.orderState.assign();
    }

    @Override
    public void accept(){
        this.orderState.accept();
    }

    @Override
    public List<DeliveryOrder> delivery(Context context,
                         String[] eleOrderIds, String actionType) throws UserException{
        return this.orderState.delivery(context,eleOrderIds, actionType);
    }

    @Override
    public Map<Long, Integer> confirmReceipt(Context context,
                                             String[] deliveryOrderIds) throws UserException {
        return this.orderState.confirmReceipt(context, deliveryOrderIds);
    }

    @Override
    public DeliveryOrder markAbnormal(Context context, String deliveryOrderId,
                             String remark) throws UserException{
        return this.orderState.markAbnormal(context, deliveryOrderId, remark);
    }

    @Override
    public Map<Long, DeliveryOrder> markNormal(Context context,
                           String[] deliveryOrderIds) throws UserException {
        return this.orderState.markNormal(context, deliveryOrderIds);
    }
}
