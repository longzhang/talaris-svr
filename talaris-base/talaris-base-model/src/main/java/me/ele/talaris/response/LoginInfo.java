/**
 * 
 */
package me.ele.talaris.response;

import java.util.List;

import me.ele.talaris.model.Role;
import me.ele.talaris.model.Station;
import me.ele.talaris.model.User;

/**
 * @author shaorongfei
 * @description
 */
public class LoginInfo {

	private String access_token;
	private int expire_minutes;
	private int is_user_info_intact; //是否补全个人信息  1：完整，0 不完整
	private int has_unconfirmed_new_license;//是否同意协议
	private User user;
	private List<Role> roles;
	private List<Station> stations;

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public int getExpire_minutes() {
		return expire_minutes;
	}

	public void setExpire_minutes(int expire_minutes) {
		this.expire_minutes = expire_minutes;
	}

	public int getIs_user_info_intact() {
		return is_user_info_intact;
	}

	public void setIs_user_info_intact(int is_user_info_intact) {
		this.is_user_info_intact = is_user_info_intact;
	}

	public int getHas_unconfirmed_new_license() {
		return has_unconfirmed_new_license;
	}

	public void setHas_unconfirmed_new_license(int has_unconfirmed_new_license) {
		this.has_unconfirmed_new_license = has_unconfirmed_new_license;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public List<Station> getStations() {
		return stations;
	}

	public void setStations(List<Station> stations) {
		this.stations = stations;
	}

}
