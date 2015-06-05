package me.ele.talaris.model;

import java.sql.Timestamp;

import me.ele.talaris.bean.Human;
import me.ele.talaris.framework.dao.Column;
import me.ele.talaris.framework.dao.Table;

/**
 * 
 * @author Kimizhang
 *
 */

@Table(name = "restaurant_bind_record")
@Human(label = "餐厅绑定记录")
public class RestaurantBindRecord {

	@Column(pk = true, auto_increase = true)
	@Human(label = "表纪录编号")
	private int id;

	@Column
	@Human(label = "餐厅老板手机号")
	private Long mobile;

	@Column
	@Human(label = "餐厅绑定状态")
	private int status;

	@Column
	@Human(label = "餐厅Id")
	private int restaurant_id;

	@Column
	@Human(label = "餐厅绑定记录创建时间")
	private Timestamp created_at;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Long getMobile() {
		return mobile;
	}

	public void setMobile(Long mobile) {
		this.mobile = mobile;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getRestaurant_id() {
		return restaurant_id;
	}

	public void setRestaurant_id(int restaurant_id) {
		this.restaurant_id = restaurant_id;
	}

	public Timestamp getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Timestamp created_at) {
		this.created_at = created_at;
	}

}
