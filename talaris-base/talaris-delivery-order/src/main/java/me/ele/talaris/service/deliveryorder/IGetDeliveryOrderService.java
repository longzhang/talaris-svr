package me.ele.talaris.service.deliveryorder;

import java.util.List;
import java.util.Map;

import me.ele.talaris.deliveryorder.dto.DeliveryOrder;
import me.ele.talaris.exception.UserException;
import me.ele.talaris.model.Context;
import me.ele.talaris.deliveryorder.dto.GetDeliveryOrderFilter;

public interface IGetDeliveryOrderService {

    /**
     * 获取配送单，大接口，该方法实际需要考虑很多，目前仅支持了简易版功能
     * 
     * @param deliveryOrderFilter
     * @param context
     * @return
     * @throws UserException
     */
    public List<DeliveryOrder> getDeliveryOrders(GetDeliveryOrderFilter deliveryOrderFilter, Context context,
            String deviceType) throws UserException;

    public void autoUpdateDeliveryingOrderStatus();

    public List<DeliveryOrder> getDeliverOrdersByDeliveryIdList(Context context, List<Long> idList, boolean isManager)
            throws UserException;

    public List<DeliveryOrder> getDeliverOrdersByDeliveryIdListWithOutAnyValid(Context context, List<Long> idList)
            throws UserException;

    // public Map<Integer, List> getDeliveryOrderListByRstId(List rstIds);

    public void testAddRedisTestOrder();

    public Map<String, String> testGetRedisOrder();

}
