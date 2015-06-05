package me.ele.talaris.deliveryorder.dto;

import java.util.Arrays;

public class GetDeliveryOrderFilter {

    @Override
    public String toString() {
        return "GetDeliveryOrderFilter [id=" + id + ", id_list=" + Arrays.toString(id_list) + ", ele_order_id="
                + ele_order_id + ", ele_order_id_list=" + Arrays.toString(ele_order_id_list) + ", status=" + status
                + ", taker=" + taker + ", passed_by=" + passed_by + ", payment_type=" + payment_type + ", to_time="
                + to_time + ", from_time=" + from_time + ", rst_id=" + rst_id + ", detail_level=" + detail_level + "]";
    }

    private String id;
    private String[] id_list;
    private String ele_order_id;
    private String[] ele_order_id_list;
    private Integer status;
    private Integer taker;
    private Integer passed_by;
    private Integer payment_type;
    private Long to_time;
    private Long from_time;
    private Integer rst_id;

    private String detail_level;

    public Integer getRst_id() {
        return rst_id;
    }

    public void setRst_id(Integer rst_id) {
        this.rst_id = rst_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String[] getId_list() {
        return id_list;
    }

    public void setId_list(String[] id_list) {
        this.id_list = id_list;
    }

    public String getEle_order_id() {
        return ele_order_id;
    }

    public void setEle_order_id(String ele_order_id) {
        this.ele_order_id = ele_order_id;
    }

    public String[] getEle_order_id_list() {
        return ele_order_id_list;
    }

    public void setEle_order_id_list(String[] ele_order_id_list) {
        this.ele_order_id_list = ele_order_id_list;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getTaker() {
        return taker;
    }

    public void setTaker(Integer taker) {
        this.taker = taker;
    }

    public Integer getPassed_by() {
        return passed_by;
    }

    public void setPassed_by(Integer passed_by) {
        this.passed_by = passed_by;
    }

    public Integer getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(Integer payment_type) {
        this.payment_type = payment_type;
    }

    public Long getTo_time() {
        return to_time;
    }

    public void setTo_time(Long to_time) {
        this.to_time = to_time;
    }

    public Long getFrom_time() {
        return from_time;
    }

    public void setFrom_time(Long from_time) {
        this.from_time = from_time;
    }

    public String getDetail_level() {
        return detail_level;
    }

    public void setDetail_level(String detail_level) {
        this.detail_level = detail_level;
    }

    public GetDeliveryOrderFilter() {
        super();
    }

}
