package me.ele.talaris.model;


import java.sql.Timestamp;

import me.ele.talaris.bean.Human;
import me.ele.talaris.framework.dao.Column;
import me.ele.talaris.framework.dao.Table;
/**
 * nt_announcement 实体类
 * Mon May 11 19:44:53 CST 2015
 * @author dawson
 */ 

@Human(label = "公告")
@Table(name="nt_announcement")
public class NtAnnouncement{

	@Column(pk = true, auto_increase = true)
	private int id;
	@Column
	@Human(label = "标题")
	private String title;
	@Column
	@Human(label = "摘要")
	private String summary;
	@Column
	@Human(label = "详细内容")
	private String content;
	@Column
	@Human(label = "弹出内容")
	private String popup_content;
	@Column
	@Human(label = "等级：1.重要通知 2.普通通知")
	private int level;
	@Column
	@Human(label = "城市ID")
	private int city_id;
	@Column
	@Human(label = "角色:1.配送员 2.餐厅老板")
	private int role_type;
	@Column
	private Timestamp created_at;
	@Column
	private Timestamp updated_at;

	public int getId(){
		return id;
	}

	public void setId(int id){
		this.id=id;
	}

	public String getTitle(){
		return title;
	}

	public void setTitle(String title){
		this.title=title;
	}

	public String getSummary(){
		return summary;
	}

	public void setSummary(String summary){
		this.summary=summary;
	}

	public String getContent(){
		return content;
	}

	public void setContent(String content){
		this.content=content;
	}

	public int getLevel(){
		return level;
	}

	public void setLevel(int level){
		this.level=level;
	}

	public int getCity_id(){
		return city_id;
	}

	public void setCity_id(int city_id){
		this.city_id=city_id;
	}

	public int getRole_type(){
		return role_type;
	}

	public void setRole_type(int role_type){
		this.role_type=role_type;
	}

	public Timestamp getCreated_at(){
		return created_at;
	}

	public void setCreated_at(Timestamp created_at){
		this.created_at=created_at;
	}

	public Timestamp getUpdated_at(){
		return updated_at;
	}

	public void setUpdated_at(Timestamp updated_at){
		this.updated_at=updated_at;
	}

	public String getPopup_content() {
		return popup_content;
	}

	public void setPopup_content(String popup_content) {
		this.popup_content = popup_content;
	}

}

