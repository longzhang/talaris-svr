/**
 * 
 */
package me.ele.talaris.deliveryorder.persistent.eb;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import me.ele.talaris.bean.Human;
import me.ele.talaris.framework.dao.Column;
import me.ele.talaris.framework.dao.Table;
import me.ele.talaris.model.CallTaskInfoEx;

/**
 * @author chaoguodeng
 *
 */
@Table(name = "delivery_order")
@Human(label = "配送单")
public class EBDeliveryOrder implements Comparable<EBDeliveryOrder> {
    @Column(pk = true, auto_increase = true)
    @Human(label = "配送单号")
    private long id;

    @Column
    @Human(label = "餐厅号")
    private int rst_id;

    @Column
    @Human(label = "配送单来源")
    private int source;

    @Column
    @Human(label = "饿单号")
    private long ele_order_id;

    @Column
    @Human(label = "饿单餐厅流水号")
    private int ele_order_sn;

    @Column
    @Human(label = "配送员号")
    private int taker_id;

    @Column
    @Human(label = "配送员手机号码")
    private long taker_mobile;

    @Column
    @Human(label = "配送站点号")
    private int station_id;

    @Column
    @Human(label = "订餐人姓名")
    private String receiver_name;

    @Column
    @Human(label = "订餐人手机号码")
    private long receiver_mobile;

    @Column
    @Human(label = "配送地址")
    private String receiver_address;

    @Column
    @Human(label = "配送单总额")
    private BigDecimal total_amount;

    @Column
    @Human(label = "配送单已付金额")
    private BigDecimal paied_amount;

    @Column
    @Human(label = "配送单支付类型")
    private int payment_type;

    @Column
    @Human(label = "配送单当前状态")
    private int status;

    @Column
    @Human(label = "配送单创建类型")
    private int created_type;

    @Column
    @Human(label = "配送单创建时间")
    private Timestamp created_at;

    @Column
    @Human(label = "配送单更新时间")
    private Timestamp updated_at;
    @Column
    private Timestamp booked_time;
    @Column
    private Timestamp ele_created_time;
    @Column 
    private String uuid;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Timestamp getBooked_time() {
        return booked_time;
    }

    public void setBooked_time(Timestamp booked_time) {
        this.booked_time = booked_time;
    }

    public Timestamp getEle_created_time() {
        return ele_created_time;
    }

    public void setEle_created_time(Timestamp ele_created_time) {
        this.ele_created_time = ele_created_time;
    }

    private String ele_order_detail;
    // 异常备注
    private String exception_remark;
    // 电话记录
    private List<CallTaskInfoEx> call_records = new ArrayList<CallTaskInfoEx>();

    private String rst_name;

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

    public String getException_remark() {
        return exception_remark;
    }

    public void setException_remark(String exception_remark) {
        this.exception_remark = exception_remark;
    }

    public String getEle_order_detail() {
        return ele_order_detail;
    }

    public void setEle_order_detail(String ele_order_detail) {
        this.ele_order_detail = ele_order_detail;
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

    public long getEle_order_id() {
        return ele_order_id;
    }

    public void setEle_order_id(long ele_order_id) {
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

    public EBDeliveryOrder(long id, int rst_id, int source, long ele_order_id, int ele_order_sn, int taker_id,
            long taker_mobile, int station_id, String receiver_name, long receiver_mobile, String receiver_address,
            BigDecimal total_amount, BigDecimal paied_amount, int payment_type, int status, int created_type,
            Timestamp created_at, Timestamp updated_at, String rst_name) {
        this.id = id;
        this.rst_id = rst_id;
        this.source = source;
        this.ele_order_id = ele_order_id;
        this.ele_order_sn = ele_order_sn;
        this.taker_id = taker_id;
        this.taker_mobile = taker_mobile;
        this.station_id = station_id;
        this.receiver_name = receiver_name;
        this.receiver_mobile = receiver_mobile;
        this.receiver_address = receiver_address;
        this.total_amount = total_amount;
        this.paied_amount = paied_amount;
        this.payment_type = payment_type;
        this.status = status;
        this.created_type = created_type;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.rst_name = rst_name;
    }

    public EBDeliveryOrder(int rst_id, int source, long ele_order_id, int ele_order_sn, int taker_id, long taker_mobile,
            int station_id, String receiver_name, long receiver_mobile, String receiver_address,
            BigDecimal total_amount, BigDecimal paied_amount, int payment_type, int status, int created_type,
            Timestamp created_at, Timestamp updated_at) {
        super();
        this.rst_id = rst_id;
        this.source = source;
        this.ele_order_id = ele_order_id;
        this.ele_order_sn = ele_order_sn;
        this.taker_id = taker_id;
        this.taker_mobile = taker_mobile;
        this.station_id = station_id;
        this.receiver_name = receiver_name;
        this.receiver_mobile = receiver_mobile;
        this.receiver_address = receiver_address;
        this.total_amount = total_amount;
        this.paied_amount = paied_amount;
        this.payment_type = payment_type;
        this.status = status;
        this.created_type = created_type;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public EBDeliveryOrder() {
        super();
    }

    @Override
    public int compareTo(EBDeliveryOrder o) {
        return new Long(this.getId()).compareTo(o.getId());
    }

}
