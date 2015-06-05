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
@Table(name = "user_device")
@Human(label = "用户设备关联表")
public class UserDevice {
	
	@Column(pk = true, auto_increase = true)
	@Human(label = "用户设备关联id")
	private int id;
	
	@Column
	@Human(label = "用户id")
	private int user_id;
	
	@Column
	@Human(label = "设备id")
	private String device_id;
	
	@Column
	@Human(label = "设备类型")
	private int device_type;
	
	@Column
	@Human(label = "设备访问密钥")
	private String access_token;
	
	@Column
	@Human(label = "客户端版本号")
	private String client_version;
	
	@Column
	@Human(label = "是否有效")
	private int is_valid;
	
	@Column
	@Human(label = "更新时间")
	private Timestamp updated_at;
	
	@Column
	@Human(label = "过期时间")
	private Timestamp expire_at;

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

	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	public int getDevice_type() {
		return device_type;
	}

	public void setDevice_type(int device_type) {
		this.device_type = device_type;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getClient_version() {
		return client_version;
	}

	public void setClient_version(String client_version) {
		this.client_version = client_version;
	}

	public int getIs_valid() {
		return is_valid;
	}

	public void setIs_valid(int is_valid) {
		this.is_valid = is_valid;
	}

	public Timestamp getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(Timestamp updated_at) {
		this.updated_at = updated_at;
	}

	public Timestamp getExpire_at() {
		return expire_at;
	}

	public void setExpire_at(Timestamp expire_at) {
		this.expire_at = expire_at;
	}

	public UserDevice(int id, int user_id, String device_id, int device_type,
			String access_token, String client_version, int is_valid,
			Timestamp updated_at, Timestamp expire_at) {
		this.id = id;
		this.user_id = user_id;
		this.device_id = device_id;
		this.device_type = device_type;
		this.access_token = access_token;
		this.client_version = client_version;
		this.is_valid = is_valid;
		this.updated_at = updated_at;
		this.expire_at = expire_at;
	}

	public UserDevice(int user_id, String device_id, int device_type, String access_token, String client_version, int is_valid,
			Timestamp updated_at, Timestamp expire_at) {
		super();
		this.user_id = user_id;
		this.device_id = device_id;
		this.device_type = device_type;
		this.access_token = access_token;
		this.client_version = client_version;
		this.is_valid = is_valid;
		this.updated_at = updated_at;
		this.expire_at = expire_at;
	}

	public UserDevice() {
		super();
	}

}
