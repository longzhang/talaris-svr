package me.ele.talaris.deliveryorder.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import me.ele.talaris.model.CallTaskInfoEx;

/**
 * 该类实际上是和DeliveryOrdery一样，但是因为前端展示的时候ele_order_id和id 太大，系统内部为long，转化给前端，前端数据越界出问题
 * id和ele_order_id 变成了String
 * 纯对前端而建的类
 * 
 * @author zhengwen
 *
 */
public class DeliveryOrderEx {
    private String id;

    private int rst_id;

    private String rst_name;

    public String getRst_name() {
        return rst_name;
    }

    public void setRst_name(String rst_name) {
        this.rst_name = rst_name;
    }

    private int source;

    private String ele_order_id;

    private int ele_order_sn;

    private int taker_id;

    private long taker_mobile;

    private int station_id;

    private String receiver_name;

    private long receiver_mobile;

    private String receiver_address;

    private BigDecimal total_amount;

    private BigDecimal paied_amount;

    private int payment_type;

    private int status;

    private int created_type;

    private Timestamp created_at;

    private Timestamp updated_at;

    private String ele_order_detail;
    // 异常备注
    private String remark;

    private Timestamp ele_created_time;
    private Timestamp booked_time;
    private String uuID;

    public Timestamp getEle_created_time() {
        return ele_created_time;
    }

    public void setEle_created_time(Timestamp ele_created_time) {
        this.ele_created_time = ele_created_time;
    }

    public Timestamp getBooked_time() {
        return booked_time;
    }

    public void setBooked_time(Timestamp booked_time) {
        this.booked_time = booked_time;
    }

    public String getUuID() {
        return uuID;
    }

    public void setUuID(String uuID) {
        this.uuID = uuID;
    }

    // 电话记录
    private List<CallTaskInfoEx> call_records = new ArrayList<CallTaskInfoEx>();

    public DeliveryOrderEx(DeliveryOrder deliveryOrder) {
        this.id = String.valueOf(deliveryOrder.getId());
        this.rst_id = deliveryOrder.getRst_id();
        this.source = deliveryOrder.getSource();
        this.ele_order_id = String.valueOf(deliveryOrder.getEle_order_id());
        this.ele_order_sn = deliveryOrder.getEle_order_sn();
        this.taker_id = deliveryOrder.getTaker_id();
        this.taker_mobile = deliveryOrder.getTaker_mobile();
        this.station_id = deliveryOrder.getStation_id();
        this.receiver_name = deliveryOrder.getReceiver_name();
        this.receiver_mobile = deliveryOrder.getReceiver_mobile();
        this.receiver_address = deliveryOrder.getReceiver_address();
        this.total_amount = deliveryOrder.getTotal_amount();
        this.paied_amount = deliveryOrder.getPaied_amount();
        this.payment_type = deliveryOrder.getPayment_type();
        this.status = deliveryOrder.getStatus();
        this.created_type = deliveryOrder.getCreated_type();
        this.created_at = deliveryOrder.getCreated_at();
        this.updated_at = deliveryOrder.getUpdated_at();
        this.rst_name = deliveryOrder.getRst_name();
        String originalEleOrderDetail = deliveryOrder.getEle_order_detail();
        if (originalEleOrderDetail != null && !"".equals(originalEleOrderDetail)) {
            String temp1 = originalEleOrderDetail.replaceFirst(":", ":\"");
            String temp2 = temp1.replaceFirst(",", "\",");
            this.ele_order_detail = temp2;
        }
        this.remark = deliveryOrder.getException_remark();
        this.call_records = deliveryOrder.getCall_records();
        this.uuID = deliveryOrder.getUuid();
        this.booked_time = deliveryOrder.getBooked_time();
        this.ele_created_time = deliveryOrder.getEle_created_time();
    }

    public DeliveryOrderEx() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getRst_id() {
        return rst_id;
    }

    public void setRst_id(int rst_id) {
        this.rst_id = rst_id;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public String getEle_order_id() {
        return ele_order_id;
    }

    public void setEle_order_id(String ele_order_id) {
        this.ele_order_id = ele_order_id;
    }

    public int getEle_order_sn() {
        return ele_order_sn;
    }

    public void setEle_order_sn(int ele_order_sn) {
        this.ele_order_sn = ele_order_sn;
    }

    public int getTaker_id() {
        return taker_id;
    }

    public void setTaker_id(int taker_id) {
        this.taker_id = taker_id;
    }

    public long getTaker_mobile() {
        return taker_mobile;
    }

    public void setTaker_mobile(long taker_mobile) {
        this.taker_mobile = taker_mobile;
    }

    public int getStation_id() {
        return station_id;
    }

    public void setStation_id(int station_id) {
        this.station_id = station_id;
    }

    public String getReceiver_name() {
        return receiver_name;
    }

    public void setReceiver_name(String receiver_name) {
        this.receiver_name = receiver_name;
    }

    public long getReceiver_mobile() {
        return receiver_mobile;
    }

    public void setReceiver_mobile(long receiver_mobile) {
        this.receiver_mobile = receiver_mobile;
    }

    public String getReceiver_address() {
        return receiver_address;
    }

    public void setReceiver_address(String receiver_address) {
        this.receiver_address = receiver_address;
    }

    public BigDecimal getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(BigDecimal total_amount) {
        this.total_amount = total_amount;
    }

    public BigDecimal getPaied_amount() {
        return paied_amount;
    }

    public void setPaied_amount(BigDecimal paied_amount) {
        this.paied_amount = paied_amount;
    }

    public int getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(int payment_type) {
        this.payment_type = payment_type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCreated_type() {
        return created_type;
    }

    public void setCreated_type(int created_type) {
        this.created_type = created_type;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }

    public String getEle_order_detail() {
        return ele_order_detail;
    }

    public void setEle_order_detail(String ele_order_detail) {
        this.ele_order_detail = ele_order_detail;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<CallTaskInfoEx> getCall_records() {
        return call_records;
    }

    public void setCall_records(List<CallTaskInfoEx> call_records) {
        this.call_records = call_records;
    }

}
