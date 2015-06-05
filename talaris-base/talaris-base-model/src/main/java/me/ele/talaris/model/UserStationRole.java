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
@Table(name = "user_station_role")
@Human(label = "用户-站点-角色关联表")
public class UserStationRole {
	@Column(pk = true, auto_increase = true)
	@Human(label = "关联id")
	private int id;
	
	@Column
	@Human(label = "用户id")
	private int user_id;
	
	@Column
	@Human(label = "站点id")
	private int station_id;
	
	@Column
	@Human(label = "角色id")
	private int role_id;
	
	@Column
	@Human(label = "关联记录状态")
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

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public int getStation_id() {
		return station_id;
	}

	public void setStation_id(int station_id) {
		this.station_id = station_id;
	}

	public int getRole_id() {
		return role_id;
	}

	public void setRole_id(int role_id) {
		this.role_id = role_id;
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

	public UserStationRole(int id, int user_id, int station_id, int role_id,
			int status, Timestamp created_at, Timestamp updated_at) {
		this.id = id;
		this.user_id = user_id;
		this.station_id = station_id;
		this.role_id = role_id;
		this.status = status;
		this.created_at = created_at;
		this.updated_at = updated_at;
	}

	public UserStationRole() {
		super();
	}

	public UserStationRole(int user_id, int station_id, int role_id, int status, Timestamp created_at, Timestamp updated_at) {
		super();
		this.user_id = user_id;
		this.station_id = station_id;
		this.role_id = role_id;
		this.status = status;
		this.created_at = created_at;
		this.updated_at = updated_at;
	}

}
