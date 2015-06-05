package me.ele.talaris.dto;

import java.sql.Timestamp;

public class AnnouncementVo{

	private int id;
 
	//标题
	private String title;

	//摘要
	private String summary;

	//详细内容
	private String content;
	
	//弹出内容
	private String popup_content;

	//等级：1.重要通知 2.普通通知
	private int level;
	
	//阅读通知
	private int status;

	//城市ID
	private int city_id;

	//角色:1.配送员 2.餐厅老板
	private int role_type;
	
	private int user_id;
 
	private Timestamp created_at;
 
	private Timestamp updated_at;
	
	
	public AnnouncementVo() {
		super();
	}

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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getPopup_content() {
		return popup_content;
	}

	public void setPopup_content(String popup_content) {
		this.popup_content = popup_content;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}	
	
	

}

