package me.ele.talaris.model;


import java.sql.Timestamp;

import me.ele.talaris.bean.Human;
import me.ele.talaris.framework.dao.Column;
import me.ele.talaris.framework.dao.Table;

/**
 * nt_announcement_read 实体类
 * Mon May 11 19:44:53 CST 2015
 * @author dawson
 */ 
@Table(name="nt_announcement_read")
public class NtAnnouncementRead{

	@Column(pk = true, auto_increase = true)
	private int id;
	@Column
	private int announcement_id;
	@Column
	private int user_id;
	@Column
	@Human(label = "'阅读状态.0:未读;1:已读'")
	private int status;
	@Column
	private Timestamp  created_at;
	@Column
	private Timestamp updated_at;

	public int getId(){
		return id;
	}

	public void setId(int id){
		this.id=id;
	}

	public int getAnnouncement_id(){
		return announcement_id;
	}

	public void setAnnouncement_id(int announcement_id){
		this.announcement_id=announcement_id;
	}

	public int getUser_id(){
		return user_id;
	}

	public void setUser_id(int user_id){
		this.user_id=user_id;
	}

	public int getStatus(){
		return status;
	}

	public void setStatus(int status){
		this.status=status;
	}

	public Timestamp getUpdated_at(){
		return updated_at;
	}

	public void setUpdated_at(Timestamp updated_at){
		this.updated_at=updated_at;
	}

	public Timestamp getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Timestamp created_at) {
		this.created_at = created_at;
	}

}

