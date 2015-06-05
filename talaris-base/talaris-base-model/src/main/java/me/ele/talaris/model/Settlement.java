/**
 * 
 */
package me.ele.talaris.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

import me.ele.talaris.bean.Human;
import me.ele.talaris.framework.dao.Column;
import me.ele.talaris.framework.dao.Table;

/**
 * @author shaorongfei
 *
 */
@Table(name = "settlement")
@Human(label = "结算纪录表")
public class Settlement implements Comparable<Settlement> {
	@Column(pk = true, auto_increase = true)
	@Human(label = "结算id")
	private long id;
	@Column
	@Human(label = "操作人id")
	private int operator_id;
	@Column
	@Human(label = "操作人姓名")
	private String operator_name;

	@Column
	@Human(label = "配送员id")
	private int taker_id;

	@Column
	@Human(label = "配送员姓名")
	private String taker_name;

	@Column
	@Human(label = "站点id")
	private int station_id;

	@Column
	@Human(label = "正常总单数")
	private int normal_total_count;

	@Column
	@Human(label = "正常总金额")
	private BigDecimal normal_total_money;

	@Column
	@Human(label = "餐到付款总单数")
	private int offline_total_count;

	@Column
	@Human(label = "餐到付款总金额")
	private BigDecimal offline_total_money;

	@Column
	@Human(label = "异常总单数")
	private int abnormal_total_count;

	@Column
	@Human(label = "异常总金额")
	private BigDecimal abnormal_total_money;

	@Column
	@Human(label = "结算单创建时间")
	private Timestamp created_at;

	@Column
	@Human(label = "结算单更新时间")
	private Timestamp updated_at;

	@Column
	@Human(label = "结算单状态")
	private int status;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getOperator_id() {
		return operator_id;
	}

	public void setOperator_id(int operator_id) {
		this.operator_id = operator_id;
	}

	public int getTaker_id() {
		return taker_id;
	}

	public void setTaker_id(int taker_id) {
		this.taker_id = taker_id;
	}

	public int getStation_id() {
		return station_id;
	}

	public String getOperator_name() {
		return operator_name;
	}

	public void setOperator_name(String operator_name) {
		this.operator_name = operator_name;
	}

	public String getTaker_name() {
		return taker_name;
	}

	public void setTaker_name(String taker_name) {
		this.taker_name = taker_name;
	}

	public void setStation_id(int station_id) {
		this.station_id = station_id;
	}

	public int getNormal_total_count() {
		return normal_total_count;
	}

	public void setNormal_total_count(int normal_total_count) {
		this.normal_total_count = normal_total_count;
	}

	public BigDecimal getNormal_total_money() {
		return normal_total_money;
	}

	public void setNormal_total_money(BigDecimal normal_total_money) {
		this.normal_total_money = normal_total_money;
	}

	public int getOffline_total_count() {
		return offline_total_count;
	}

	public void setOffline_total_count(int offline_total_count) {
		this.offline_total_count = offline_total_count;
	}

	public BigDecimal getOffline_total_money() {
		return offline_total_money;
	}

	public void setOffline_total_money(BigDecimal offline_total_money) {
		this.offline_total_money = offline_total_money;
	}

	public int getAbnormal_total_count() {
		return abnormal_total_count;
	}

	public void setAbnormal_total_count(int abnormal_total_count) {
		this.abnormal_total_count = abnormal_total_count;
	}

	public BigDecimal getAbnormal_total_money() {
		return abnormal_total_money;
	}

	public void setAbnormal_total_money(BigDecimal abnormal_total_money) {
		this.abnormal_total_money = abnormal_total_money;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @param operator_id
	 * @param operator_name
	 * @param taker_id
	 * @param taker_name
	 * @param station_id
	 * @param normal_total_count
	 * @param normal_total_money
	 * @param offline_total_count
	 * @param offline_total_money
	 * @param abnormal_total_count
	 * @param abnormal_total_money
	 * @param created_at
	 * @param updated_at
	 * @param status
	 */
	public Settlement(int operator_id, String operator, int taker_id, String taker, int station_id,
			int normal_total_count, BigDecimal normal_total_money, int offline_total_count,
			BigDecimal offline_total_money, int abnormal_total_count, BigDecimal abnormal_total_money,
			Timestamp created_at, Timestamp updated_at, int status) {
		super();
		this.operator_id = operator_id;
		this.operator_name = operator;
		this.taker_id = taker_id;
		this.taker_name = taker;
		this.station_id = station_id;
		this.normal_total_count = normal_total_count;
		this.normal_total_money = normal_total_money;
		this.offline_total_count = offline_total_count;
		this.offline_total_money = offline_total_money;
		this.abnormal_total_count = abnormal_total_count;
		this.abnormal_total_money = abnormal_total_money;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.status = status;
	}

	/**
	 * 
	 */
	public Settlement() {
		super();
	}

	@Override
	public int compareTo(Settlement o) {
		return new Long(o.getId()).compareTo(this.getId());
	}

}
