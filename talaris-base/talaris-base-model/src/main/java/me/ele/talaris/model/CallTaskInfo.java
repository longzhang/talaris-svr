package me.ele.talaris.model;

import java.sql.Timestamp;

import me.ele.talaris.framework.dao.Column;
import me.ele.talaris.framework.dao.Table;

/**
 * 
 * @author Kimizhang
 *
 */

@Table(name = "delivery_order_call_task")
public class CallTaskInfo {
	@Column(pk = true, auto_increase = true)
	private long id;
	@Column
	private long task_id;
	@Column
	private long delivery_order_id;
	@Column
	private Timestamp created_at;
	@Column
	private int update_time;
	@Column
	private int status;
	@Column
	private String error_log;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getTask_id() {
		return task_id;
	}

	public void setTask_id(long task_id) {
		this.task_id = task_id;
	}

	public long getDelivery_order_id() {
		return delivery_order_id;
	}

	public void setDelivery_order_id(long delivery_order_id) {
		this.delivery_order_id = delivery_order_id;
	}

	public Timestamp getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Timestamp created_at) {
		this.created_at = created_at;
	}

	public int getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(int update_time) {
		this.update_time = update_time;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getError_log() {
		return error_log;
	}

	public void setError_log(String error_log) {
		this.error_log = error_log;
	}

	public CallTaskInfo() {
		super();
	}

}
