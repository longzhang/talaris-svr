package me.ele.talaris.service.deliveryorder;

import java.util.Map;

import me.ele.talaris.deliveryorder.dto.DeliveryOrder;
import me.ele.talaris.exception.UserException;
import me.ele.talaris.model.Context;

public interface IMarkNormalService {

	/**
	 * 标记为正常，返回的是正常的配送单信息的map，key为配送单ID
	 * 
	 * @param context
	 * @param deliveryOrderIds
	 * @return
	 * @throws UserException
	 */
	public Map<Long, DeliveryOrder> markNormal(Context context,
			String[] deliveryOrderIds) throws UserException;

}