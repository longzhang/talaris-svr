package me.ele.talaris.webapi.inter;

class Data {
	private long task_id;
	
	private int new_status;
	
	private float update_time;
	
	private String error_log;

	public long getTask_id() {
		return task_id;
	}

	public void setTask_id(long task_id) {
		this.task_id = task_id;
	}

	public int getNew_status() {
		return new_status;
	}

	public void setNew_status(int new_status) {
		this.new_status = new_status;
	}

	public float getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(float update_time) {
		this.update_time = update_time;
	}

	public String getError_log() {
		return error_log;
	}

	public void setError_log(String error_log) {
		this.error_log = error_log;
	}
	
}
public class HermesStatus {
	 
	private int n_type;
	 
	private Data data;

	public int getN_type() {
		return n_type;
	}

	public void setN_type(int n_type) {
		this.n_type = n_type;
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}
	
}
