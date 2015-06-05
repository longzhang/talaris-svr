package me.ele.talaris.service.settlement;

import me.ele.talaris.exception.UserException;
import me.ele.talaris.model.Context;
import me.ele.talaris.model.settlement.ViewDeliveryOrderInfos;

public interface ISettlementGetDeliveryOrderService {

    public ViewDeliveryOrderInfos getDeliveryOrder(Context context, int takerId, int status, Integer paymentType,
            Integer pageNow, Integer pageSize, int rst_id, String detailLevel) throws UserException;

    public ViewDeliveryOrderInfos takerGetOwnDeliveryOrder(Context context, int status, int paymentType, int takerId,
            int rst_id, int detailLevel) throws UserException;

}
