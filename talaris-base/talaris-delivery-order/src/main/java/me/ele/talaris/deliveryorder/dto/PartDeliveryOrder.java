package me.ele.talaris.deliveryorder.dto;

import java.sql.Timestamp;

/**
 * 获取代配送的配送单，为了省流量做的一个返回
 * 
 * @author zhengwen
 *
 */
public class PartDeliveryOrder {
    private int ele_order_sn;
    private String ele_order_id;
    private int is_booked;
    private Timestamp booked_time;

    public int getEle_order_sn() {
        return ele_order_sn;
    }

    public void setEle_order_sn(int ele_order_sn) {
        this.ele_order_sn = ele_order_sn;
    }

    public String getEle_order_id() {
        return ele_order_id;
    }

    public void setEle_order_id(String ele_order_id) {
        this.ele_order_id = ele_order_id;
    }

    public PartDeliveryOrder(int ele_order_sn, String ele_order_id, int is_booked, Timestamp booked_time) {
        this.ele_order_sn = ele_order_sn;
        this.ele_order_id = ele_order_id;
        this.is_booked = is_booked;
        this.booked_time = booked_time;
    }

    public int getIs_booked() {
        return is_booked;
    }

    public void setIs_booked(int is_booked) {
        this.is_booked = is_booked;
    }

    public Timestamp getBooked_time() {
        return booked_time;
    }

    public void setBooked_time(Timestamp booked_time) {
        this.booked_time = booked_time;
    }

    public PartDeliveryOrder() {
        super();
    }

}
