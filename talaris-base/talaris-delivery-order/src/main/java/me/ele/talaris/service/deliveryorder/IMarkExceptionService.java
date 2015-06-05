package me.ele.talaris.service.deliveryorder;

import me.ele.talaris.deliveryorder.dto.DeliveryOrder;
import me.ele.talaris.exception.UserException;
import me.ele.talaris.model.Context;

public interface IMarkExceptionService {

	/**
	 * 标记为异常,返回的是异常配送单的信息
	 * 
	 * @param context
	 * @param deliveryOrderId
	 * @param remark
	 * @return
	 * @throws UserException
	 */
	public DeliveryOrder markException(Context context, String deliveryOrderId,
			String remark) throws UserException;

}