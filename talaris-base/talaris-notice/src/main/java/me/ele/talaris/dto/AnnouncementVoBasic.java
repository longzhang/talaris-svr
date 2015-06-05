package me.ele.talaris.dto;

import java.util.Date;

public class AnnouncementVoBasic{

	private int id;
	//标题
	private String title;
	//摘要
	private String summary;

	//等级：1.重要通知 2.普通通知
	private int level;
	
	//阅读通知
	private int status;
	
	private long created_at;

	public AnnouncementVoBasic() {
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


	public int getLevel(){
		return level;
	}

	public void setLevel(int level){
		this.level=level;
	}

	public long getCreated_at(){
		return created_at;
	}

	public void setCreated_at(long created_at){
		this.created_at=created_at;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}	

}

