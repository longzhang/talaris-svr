/**
 * 
 */
package me.ele.talaris.model;

import java.sql.Timestamp;

import me.ele.talaris.bean.Human;
import me.ele.talaris.framework.dao.Column;
import me.ele.talaris.framework.dao.Table;

/**
 * @author shaorongfei
 *
 */
@Table(name = "settlement_delivery_order")
@Human(label = "结算配送单关联表")
public class SettlementDeliveryOrder {
	/**
	 * 
	 */
	public SettlementDeliveryOrder() {
		super();
	}

	@Column(pk = true, auto_increase = true)
	@Human(label = "自增id")
	private int id;

	@Column
	@Human(label = "结算id")
	private long settlement_id;

	@Column
	@Human(label = "配送员id")
	private int taker_id;

	@Column
	@Human(label = "批次结算配送单集合")
	private String delivery_order_list;

	@Column
	@Human(label = "结算配送单关联状态")
	private int status;

	@Column
	@Human(label = "创建时间")
	private Timestamp created_at;

	@Column
	@Human(label = "更新时间")
	private Timestamp updated_at;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getSettlement_id() {
		return settlement_id;
	}

	public void setSettlement_id(long settlement_id) {
		this.settlement_id = settlement_id;
	}

	public int getTaker_id() {
		return taker_id;
	}

	public void setTaker_id(int taker_id) {
		this.taker_id = taker_id;
	}

	public String getDelivery_order_list() {
		return delivery_order_list;
	}

	public void setDelivery_order_list(String delivery_order_list) {
		this.delivery_order_list = delivery_order_list;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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

	/**
	 * @param settlement_id
	 * @param taker_id
	 * @param delivery_order_list
	 * @param status
	 * @param created_at
	 * @param updated_at
	 */
	public SettlementDeliveryOrder(long settlement_id, int taker_id, String delivery_order_list, int status,
			Timestamp created_at, Timestamp updated_at) {
		super();
		this.settlement_id = settlement_id;
		this.taker_id = taker_id;
		this.delivery_order_list = delivery_order_list;
		this.status = status;
		this.created_at = created_at;
		this.updated_at = updated_at;
	}
}
