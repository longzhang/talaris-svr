package me.ele.talaris.service.deliveryorder;

import java.util.Map;

import me.ele.talaris.exception.UserException;
import me.ele.talaris.model.Context;

public interface IConfirmDeliverdService {

	/**
	 * 确认送达，返回的是map。单号对应是否成功，0对应失败，1对应成功
	 * 
	 * @param context
	 * @param deliveryOrderIds
	 * @return
	 * @throws UserException
	 */
	public Map<Long, Integer> confirmDelivered(Context context,
			String[] deliveryOrderIds) throws UserException;

}