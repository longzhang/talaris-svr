package me.ele.talaris.service.deliveryorder;

import java.util.Map;

import me.ele.talaris.exception.UserException;
import me.ele.talaris.model.Context;

public interface ICancelDeliveryOrderService {

	public Map<String, Integer> cancel(Context context, String deliveryOrderId)
			throws UserException;

}