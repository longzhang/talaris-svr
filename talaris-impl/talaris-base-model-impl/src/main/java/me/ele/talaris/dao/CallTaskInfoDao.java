package me.ele.talaris.dao;

import java.util.List;

import me.ele.talaris.framework.dao.BaseSpringDao;
import me.ele.talaris.model.CallTaskInfo;

import org.springframework.jdbc.core.BeanPropertyRowMapper;

/**
 * 
 * @author Kimizhang
 *
 */

public class CallTaskInfoDao extends BaseSpringDao<CallTaskInfo> {

	public CallTaskInfoDao() {
		super(new BeanPropertyRowMapper<CallTaskInfo>(CallTaskInfo.class));
	}

	public CallTaskInfo getCallTaskInfoByTaskId(long taskId) {

		return this.selectOneOrNull("where task_id = ?", taskId);
	}

	public List<CallTaskInfo> getCallTaskInfoByDeliveryOrderId(long deliveryOrderId) {
		return this.select("where delivery_order_id = ?", deliveryOrderId);
	}
}
