package me.ele.talaris.service.deliveryorder;

import java.util.Map;

import me.ele.talaris.exception.UserException;
import me.ele.talaris.model.Context;

public interface INotifyUserService {

	/**
	 * 电话通知用户外卖已经送达，返回map的key为配送单ID，value为电话事件ID
	 * 
	 * @param context
	 * @param deliveryOrderIds
	 * @return
	 * @throws UserException
	 */
	public Map<Long, Long> notifyCustomer(Context context,
			String[] deliveryOrderIds) throws UserException;

}