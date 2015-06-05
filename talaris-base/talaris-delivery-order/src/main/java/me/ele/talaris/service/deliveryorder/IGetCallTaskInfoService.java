package me.ele.talaris.service.deliveryorder;

import java.util.List;

import me.ele.talaris.model.CallTaskInfoEx;

public interface IGetCallTaskInfoService {

	/**
	 * 根据电话通知taskID来获取该电话的状态
	 * 
	 * @param taskIdList
	 * @return
	 */
	public List<CallTaskInfoEx> getCallTaskInfosByIdList(List<Long> taskIdList);

}