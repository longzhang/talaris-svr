package me.ele.talaris.service.deliveryorder;

import me.ele.talaris.deliveryorder.dto.DeliveryOrder;
import me.ele.talaris.exception.UserException;
import me.ele.talaris.model.Context;

import java.util.List;
import java.util.Map;

/**
 * Created by Daniel on 15/5/6.
 */
public interface IDeliveryOrderContextService {
    public void create(Context context,
                       String[] eleOrderIds, String actionType)  throws UserException;

    public void assign();

    public void accept();

    public List<DeliveryOrder> delivery(Context context,
                                        String[] eleOrderIds, String actionType) throws UserException;

    public Map<Long, Integer> confirmReceipt(Context context,
                                             String[] deliveryOrderIds) throws UserException ;

    public DeliveryOrder markAbnormal(Context context, String deliveryOrderId,
                                      String remark) throws UserException;

    public Map<Long, DeliveryOrder> markNormal(Context context,
                           String[] deliveryOrderIds) throws UserException;
}
