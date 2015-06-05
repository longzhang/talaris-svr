package me.ele.talaris.dto;

import java.util.List;

public class AnnoucementImportant {
	
	private int count;
	
	private List<AnnouncementVoContent> popups;
	
	public AnnoucementImportant() {
		super();
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<AnnouncementVoContent> getPopups() {
		return popups;
	}

	public void setPopups(List<AnnouncementVoContent> popups) {
		this.popups = popups;
	}
	
	
	
}
