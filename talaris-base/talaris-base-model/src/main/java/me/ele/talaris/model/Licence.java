/**
 * 
 */
package me.ele.talaris.model;

import java.sql.Timestamp;

import me.ele.talaris.bean.Human;
import me.ele.talaris.framework.dao.Column;
import me.ele.talaris.framework.dao.Table;

/**
 * @author shaorongfei
 *
 */
@Table(name = "license")
@Human(label = "协议表")
public class Licence {
	@Column(pk = true, auto_increase = true)
	@Human(label = "协议编号")
	private int id;
	
	@Column
	@Human(label = "协议类型")
	private int type;

	@Column
	@Human(label = "协议版本")
	private String version;
	
	@Column
	@Human(label = "协议内容")
	private String content;
	
	@Column
	@Human(label = "协议状态 0:无效;1:有效不可发布;2:有效可发布")
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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
}
