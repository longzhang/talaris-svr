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
@Table(name = "user_license")
@Human(label = "用户协议相关表")
public class UserLicence {
	
	@Column(pk = true, auto_increase = true)
	@Human(label = "自增编号")
	private int id;
	
	@Column
	@Human(label = "用户id")
	private int user_id;
	
	public UserLicence() {
		super();
	}

	@Column
	@Human(label = "协议id")
	private int license_id;
	
	@Column
	@Human(label = "创建时间")
	private Timestamp created_at;

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public int getLicense_id() {
		return license_id;
	}

	public void setLicense_id(int license_id) {
		this.license_id = license_id;
	}

	public Timestamp getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Timestamp created_at) {
		this.created_at = created_at;
	}

	public UserLicence(int user_id, int license_id, Timestamp created_at) {
		super();
		this.user_id = user_id;
		this.license_id = license_id;
		this.created_at = created_at;
	}

}
