/**
 * 
 */
package me.ele.talaris.deliveryorder.persistent.eb;

import java.sql.Timestamp;

import me.ele.talaris.bean.Human;
import me.ele.talaris.deliveryorder.dto.DeliveryOrder;
import me.ele.talaris.framework.dao.Column;
import me.ele.talaris.framework.dao.Table;

/**
 * @author chaoguodeng
 *
 */

@Table(name = "delivery_order_record")
@Human(label = "配送单记录")
public class DeliveryOrderRecord {
    @Column(pk = true, auto_increase = true)
    @Human(label = "配送单记录号")
    private long id;

    @Column
    @Human(label = "配送单号")
    private long delivery_order_id;

    @Column
    @Human(label = "饿单号")
    private long ele_order_id;

    @Column
    @Human(label = "配送员号")
    private int taker_id;

    @Column
    @Human(label = "配送员手机号码")
    private long taker_mobile;

    @Column
    @Human(label = "配送站号")
    private int station_id;

    @Column
    @Human(label = "操作者id")
    private int operator_id;

    @Column
    @Human(label = "操作编号")
    private int operation_id;

    @Column
    @Human(label = "配送状态")
    private int status;

    @Column
    @Human(label = "备注")
    private String remark;

    @Column
    @Human(label = "配送单记录创建时间")
    private Timestamp created_at;

    public long getEle_order_id() {
        return ele_order_id;
    }

    public void setEle_order_id(long ele_order_id) {
        this.ele_order_id = ele_order_id;
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

    public int getOperator_id() {
        return operator_id;
    }

    public void setOperator_id(int operator_id) {
        this.operator_id = operator_id;
    }

    public int getoperation_id() {
        return operation_id;
    }

    public void setoperation_id(int operation_id) {
        this.operation_id = operation_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public long getDelivery_order_id() {
        return delivery_order_id;
    }

    public void setDelivery_order_id(long delivery_order_id) {
        this.delivery_order_id = delivery_order_id;
    }

    public DeliveryOrderRecord(long id, int delivery_order_id, long ele_order_id, int taker_id, long taker_mobile,
            int station_id, int operator_id, int operation_id, int status, String remark, Timestamp created_at) {
        super();
        this.id = id;
        this.delivery_order_id = delivery_order_id;
        this.ele_order_id = ele_order_id;
        this.taker_id = taker_id;
        this.taker_mobile = taker_mobile;
        this.station_id = station_id;
        this.operator_id = operator_id;
        this.operation_id = operation_id;
        this.status = status;
        this.remark = remark;
        this.created_at = created_at;
    }

    public DeliveryOrderRecord(int delivery_order_id, long ele_order_id, int taker_id, long taker_mobile,
            int station_id, int operator_id, int operation_id, int status, String remark, Timestamp created_at) {
        this.delivery_order_id = delivery_order_id;
        this.ele_order_id = ele_order_id;
        this.taker_id = taker_id;
        this.taker_mobile = taker_mobile;
        this.station_id = station_id;
        this.operator_id = operator_id;
        this.operation_id = operation_id;
        this.status = status;
        this.remark = remark;
        this.created_at = created_at;
    }

    public DeliveryOrderRecord(DeliveryOrder order, int operator_id, int operation_id, String remark){
        this.delivery_order_id = order.getId();
        this.ele_order_id = order.getEle_order_id();
        this.taker_id = order.getTaker_id();
        this.taker_mobile = order.getTaker_mobile();
        this.station_id = order.getStation_id();
        this.operator_id = operator_id;
        this.operation_id = operation_id;
        this.status = order.getStatus();
        this.remark = remark;
        this.created_at = new Timestamp(System.currentTimeMillis());
    }

    public DeliveryOrderRecord() {
        super();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getOperation_id() {
        return operation_id;
    }

    public void setOperation_id(int operation_id) {
        this.operation_id = operation_id;
    }

}
