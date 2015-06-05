package me.ele.talaris.service.deliveryorder;

import java.util.List;

import me.ele.talaris.deliveryorder.dto.DeliveryOrder;
import me.ele.talaris.deliveryorder.dto.DeliveryOrderEx;
import me.ele.talaris.deliveryorder.dto.PartDeliveryOrder;
import me.ele.talaris.exception.UserException;
import me.ele.talaris.model.Context;

public interface IGetWaitToDeliveryOrderService {

    /**
     * 配送员查看自己所在站点对应的配送单，这里的配送单拉取出来后全部是待配送的
     * 
     * @param context
     * @return
     * @throws UserException
     */
    public List<DeliveryOrder> getDeliveryOrdersByContext(Context context, int rst_id) throws UserException;

    public List<PartDeliveryOrder> changeLongDeliveryOrderToSort(List<DeliveryOrderEx> deliveryOrderExs);

}
