/**
 * 
 */
package me.ele.talaris.model;

import java.sql.Timestamp;

import me.ele.talaris.bean.Human;
import me.ele.talaris.framework.dao.Column;
import me.ele.talaris.framework.dao.Table;

/**
 * @author chaoguodeng
 *
 */
@Table(name = "role")
@Human(label = "角色")
public class Role {
	@Column(pk = true, auto_increase = true)
	@Human(label = "角色id")
	private int id;
	
	@Column
	@Human(label = "角色名称")
	private String role_name;
	
	@Column
	@Human(label = "角色描述")
	private String role_description;
	
	@Column
	@Human(label = "角色状态")
	private int status;
	
	@Column
	@Human(label = "角色创建时间")
	private Timestamp created_at;
	
	@Column
	@Human(label = "角色更新时间")
	private Timestamp updated_at;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRole_name() {
		return role_name;
	}

	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}

	public String getRole_description() {
		return role_description;
	}

	public void setRole_description(String role_description) {
		this.role_description = role_description;
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

	public Role(int id, String role_name, String role_description, int status,
			Timestamp created_at, Timestamp updated_at) {
		this.id = id;
		this.role_name = role_name;
		this.role_description = role_description;
		this.status = status;
		this.created_at = created_at;
		this.updated_at = updated_at;
	}

	public Role() {
		super();
	}

}
