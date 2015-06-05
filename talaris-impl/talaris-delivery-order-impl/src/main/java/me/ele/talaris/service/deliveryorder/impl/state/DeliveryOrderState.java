package me.ele.talaris.service.deliveryorder.impl.state;

import me.ele.talaris.deliveryorder.dto.DeliveryOrder;
import me.ele.talaris.exception.UserException;
import me.ele.talaris.model.Context;

import java.util.List;
import java.util.Map;

/**
 * Created by Daniel on 15/5/5.
 */
public abstract class DeliveryOrderState {
    protected DeliveryOrderContext deliveryOrderContext;

    public void setDeliveryOrderContext(DeliveryOrderContext context){
        this.deliveryOrderContext = context;
    }

    /**
     * 创建订单
     */
    public abstract void create(Context context,
                                String[] eleOrderIds, String actionType) throws UserException;

    /**
     * 分配订单
     */
    public abstract void assign();

    /**
     * 配送员收到订单 并确认（拉单）
     */
    public abstract void accept();

    /**
     * 配送
     */
    public abstract List<DeliveryOrder> delivery(Context context,
                                  String[] eleOrderIds, String actionType) throws UserException;

    /**
     * 确认送达
     */
    public abstract Map<Long, Integer> confirmReceipt(Context context,
                                        String[] deliveryOrderIds) throws UserException;

    /**
     * 标记为异常
     */
    public abstract DeliveryOrder markAbnormal(Context context, String deliveryOrderId,
                                      String remark) throws UserException;

    public abstract Map<Long, DeliveryOrder> markNormal(Context context,
                                    String[] deliveryOrderIds) throws UserException;
}
