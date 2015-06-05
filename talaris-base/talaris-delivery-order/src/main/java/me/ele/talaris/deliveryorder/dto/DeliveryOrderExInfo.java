package me.ele.talaris.deliveryorder.dto;

import java.sql.Timestamp;

/**
 * 应用场景：
 * 现在有定时任务1：如果一个单子变成配送中，三个小时后回默认变成配送完成
 *
 * 所有的单子用hash结构存放在redis里面
 * hash结构为：key:DELIVERYING_ORDER_IDS field:id(单号) value "{"status_change_time":12343435343,"is_booked":1}"
 *
 * 方便定时任务来做扫描。目前json里只会有status_change_time":12343435343 字段，is_booked暂时不给
 *
 *
 * @author zhengwen
 *
 */

public class DeliveryOrderExInfo {
    private Timestamp status_change_time;
    private int is_booked;

    public Timestamp getStatus_change_time() {
        return status_change_time;
    }

    public void setStatus_change_time(Timestamp status_change_time) {
        this.status_change_time = status_change_time;
    }

    public int getIs_booked() {
        return is_booked;
    }

    public void setIs_booked(int is_booked) {
        this.is_booked = is_booked;
    }

    public DeliveryOrderExInfo(Timestamp status_create_time) {
        super();
        this.status_change_time = status_create_time;
    }

    public DeliveryOrderExInfo(Timestamp status_create_time, int is_booked) {
        super();
        this.status_change_time = status_create_time;
        this.is_booked = is_booked;
    }

    public DeliveryOrderExInfo() {
        super();
    }
}
