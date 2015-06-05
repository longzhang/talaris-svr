package me.ele.talaris.model.settlement;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import me.ele.talaris.deliveryorder.dto.DeliveryOrder;
import me.ele.talaris.model.CallTaskInfoEx;

/**
 * 站点管理员查看某个配送员的订单情况，目前页面显示只是一个配送单的部分信息，为节省手机端流量，所以建了该'DTO'。
 * 该DTO为了方便以后拆分，所以放在了和被调用的controller一起
 * 
 * @author zhengwen
 *
 */
public class DeliveryOrderPartInfo implements Comparable<DeliveryOrderPartInfo> {
    private int rst_id;
    private String rst_name;
    private String id;
    private String ele_order_id;
    private int ele_order_sn;
    private String receiver_name;
    private String receiver_address;
    private long receiver_mobile;
    private int status;
    private Timestamp operator_time;
    private Timestamp create_order_time;
    private BigDecimal order_actual_cash;
    private int has_deliveried_minutes;
    private String remark;
    private String ele_order_detail;
    private int payment_type;
    private List<CallTaskInfoEx> call_records = new ArrayList<CallTaskInfoEx>();

    public int getRst_id() {
        return rst_id;
    }

    public void setRst_id(int rst_id) {
        this.rst_id = rst_id;
    }

    public String getRst_name() {
        return rst_name;
    }

    public void setRst_name(String rst_name) {
        this.rst_name = rst_name;
    }

    public List<CallTaskInfoEx> getCall_records() {
        return call_records;
    }

    public void setCall_records(List<CallTaskInfoEx> call_records) {
        this.call_records = call_records;
    }

    public String getReceiver_name() {
        return receiver_name;
    }

    public void setReceiver_name(String receiver_name) {
        this.receiver_name = receiver_name;
    }

    public int getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(int payment_type) {
        this.payment_type = payment_type;
    }

    public long getReceiver_mobile() {
        return receiver_mobile;
    }

    public void setReceiver_mobile(long receiver_mobile) {
        this.receiver_mobile = receiver_mobile;
    }

    public Timestamp getCreate_order_time() {
        return create_order_time;
    }

    public void setCreate_order_time(Timestamp create_order_time) {
        this.create_order_time = create_order_time;
    }

    public int getHas_deliveried_minutes() {
        return has_deliveried_minutes;
    }

    public void setHas_deliveried_minutes(int has_deliveried_minutes) {
        this.has_deliveried_minutes = has_deliveried_minutes;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getEle_order_detail() {
        return ele_order_detail;
    }

    public void setEle_order_detail(String ele_order_detail) {
        this.ele_order_detail = ele_order_detail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getReceiver_address() {
        return receiver_address;
    }

    public void setReceiver_address(String receiver_address) {
        this.receiver_address = receiver_address;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Timestamp getOperator_time() {
        return operator_time;
    }

    public void setOperator_time(Timestamp operator_time) {
        this.operator_time = operator_time;
    }

    public BigDecimal getOrder_actual_cash() {
        return order_actual_cash;
    }

    public void setOrder_actual_cash(BigDecimal order_actual_cash) {
        this.order_actual_cash = order_actual_cash;
    }

    public DeliveryOrderPartInfo() {
        super();
    }

    public DeliveryOrderPartInfo(String id, String ele_order_id, int ele_order_sn, String receiver_address,
            long receiver_mobile, int status, Timestamp operator_time, Timestamp create_order_time,
            BigDecimal order_actual_cash, int has_deliveried_minutes, String remark, String ele_order_detail,
            int payment_type, String receiver_name, List<CallTaskInfoEx> callTaskInfoExs) {
        super();
        this.id = id;
        this.ele_order_id = ele_order_id;
        this.ele_order_sn = ele_order_sn;
        this.receiver_address = receiver_address;
        this.receiver_mobile = receiver_mobile;
        this.status = status;
        this.operator_time = operator_time;
        this.create_order_time = create_order_time;
        this.order_actual_cash = order_actual_cash;
        this.has_deliveried_minutes = has_deliveried_minutes;
        this.remark = remark;
        this.ele_order_detail = ele_order_detail;
        this.payment_type = payment_type;
        this.receiver_name = receiver_name;
        this.call_records = callTaskInfoExs;
    }

    public DeliveryOrderPartInfo(DeliveryOrder deliveryOrder, String rstName) {
        this.id = String.valueOf(deliveryOrder.getId());
        this.ele_order_id = String.valueOf(deliveryOrder.getEle_order_id());
        this.ele_order_sn = deliveryOrder.getEle_order_sn();
        this.receiver_address = deliveryOrder.getReceiver_address();
        this.receiver_mobile = deliveryOrder.getReceiver_mobile();
        this.status = deliveryOrder.getStatus();
        this.operator_time = deliveryOrder.getUpdated_at();
        this.order_actual_cash = deliveryOrder.getTotal_amount();
        this.has_deliveried_minutes = new BigDecimal((System.currentTimeMillis() - deliveryOrder.getCreated_at()
                .getTime())).divide(new BigDecimal(60000), 2).intValue();
        this.remark = deliveryOrder.getException_remark();
        this.payment_type = deliveryOrder.getPayment_type();
        this.ele_order_detail = deliveryOrder.getEle_order_detail();
        this.receiver_name = deliveryOrder.getReceiver_name();
        this.call_records = deliveryOrder.getCall_records();
        this.rst_id = deliveryOrder.getRst_id();
        this.rst_name = rstName;
    }

    @Override
    public int compareTo(DeliveryOrderPartInfo o) {
        return new Integer(o.getHas_deliveried_minutes()).compareTo(this.getHas_deliveried_minutes());
    }

}
