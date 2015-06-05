package me.ele.talaris.dto;

import java.util.List;

public class AnnoucementSummery {
	
	private long sys_time;
	
	private List<AnnouncementVoBasic> list;
	
	public AnnoucementSummery() {
		super();
	}
	public long getSys_time() {
		return sys_time;
	}
	public void setSys_time(long sys_time) {
		this.sys_time = sys_time;
	}
	public List<AnnouncementVoBasic> getList() {
		return list;
	}
	public void setList(List<AnnouncementVoBasic> list) {
		this.list = list;
	}
	
}
