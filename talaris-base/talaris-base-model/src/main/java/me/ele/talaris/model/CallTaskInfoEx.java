package me.ele.talaris.model;

import java.sql.Timestamp;

/**
 * 该类实际上是和CallTaskInfo一样，但是因为前端展示的时候task_id，delivery_order_id 太大，系统内部为long，转化给前端，前端数据越界出问题
 * task_id和ele_order_id 变成了String
 * 纯对前端而建的类
 * 
 * @author zhengwen
 *
 */
public class CallTaskInfoEx {
    private String id;
    private String task_id;
    private String delivery_order_id;
    private Timestamp created_at;
    private int update_time;
    private int status;
    private String error_log;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getDelivery_order_id() {
        return delivery_order_id;
    }

    public void setDelivery_order_id(String delivery_order_id) {
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

    public CallTaskInfoEx(CallTaskInfo callTaskInfo) {
        this.id = String.valueOf(callTaskInfo.getId());
        this.task_id = String.valueOf(callTaskInfo.getTask_id());
        this.delivery_order_id = String.valueOf(callTaskInfo.getDelivery_order_id());
        this.created_at = callTaskInfo.getCreated_at();
        this.update_time = callTaskInfo.getUpdate_time();
        this.status = callTaskInfo.getStatus();
        this.error_log = callTaskInfo.getError_log();
    }

    public CallTaskInfoEx() {
        super();
    }

}
