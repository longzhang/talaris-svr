/**
 * 
 */
package me.ele.talaris.model;

import java.math.BigDecimal;

import me.ele.talaris.bean.Human;
import me.ele.talaris.framework.dao.Column;
import me.ele.talaris.framework.dao.Table;

/**
 * @author chaoguodeng
 *
 */
@Table(name = "restaurant")
@Human(label = "餐厅")
public class Restaurant {
	@Column(pk = true, auto_increase = true)
	@Human(label = "表记录编号")
	private int id;

	@Column
	@Human(label = "餐厅编号")
	private int ele_id;

	@Column
	@Human(label = "餐厅名称")
	private String name;

	@Column
	@Human(label = "餐厅号码")
	private String phone;

	@Column
	@Human(label = "餐厅地址")
	private String address;

	@Column
	@Human(label = "城市id")
	private int city_id;

	@Column
	@Human(label = "餐厅经度")
	private BigDecimal longitude;

	@Column
	@Human(label = "餐厅纬度")
	private BigDecimal latitude;

	@Column
	@Human(label = "餐厅管理员手机号码")
	private long manager_mobile;

	@Column
	@Human(label = "餐厅状态")
	private int status;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getEle_id() {
		return ele_id;
	}

	public void setEle_id(int ele_id) {
		this.ele_id = ele_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public long getManager_mobile() {
		return manager_mobile;
	}

	public void setManager_mobile(long manager_mobile) {
		this.manager_mobile = manager_mobile;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getCity_id() {
		return city_id;
	}

	public void setCity_id(int city_id) {
		this.city_id = city_id;
	}

	public Restaurant(int id, int ele_id, String name, String phone,
			String address, int city_id, BigDecimal longitude,
			BigDecimal latitude, int manager_ele_id, String manager_name,
			long manager_mobile, int status) {
		this.id = id;
		this.ele_id = ele_id;
		this.name = name;
		this.phone = phone;
		this.address = address;
		this.city_id = city_id;
		this.longitude = longitude;
		this.latitude = latitude;
		this.manager_mobile = manager_mobile;
		this.status = status;
	}

	public Restaurant(int ele_id, String name, String phone, String address,
			int city_id, BigDecimal longitude, BigDecimal latitude,
			int manager_ele_id, String manager_name, long manager_mobile,
			int status) {
		this.ele_id = ele_id;
		this.name = name;
		this.phone = phone;
		this.address = address;
		this.city_id = city_id;
		this.longitude = longitude;
		this.latitude = latitude;
		this.manager_mobile = manager_mobile;
		this.status = status;
	}

	public Restaurant() {
		super();
	}

}
