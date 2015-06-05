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
@Table(name = "operation")
@Human(label = "操作表")
public class Operation {
	@Column(pk = true, auto_increase = true)
	@Human(label = "操作编号")
	private int id;
	
	@Column
	@Human(label = "操作名称")
	private String name;
	
	@Column
	@Human(label = "操作描述")
	private String description;
	
	@Column
	@Human(label = "操作创建时间")
	private Timestamp created_at;
	
	@Column
	@Human(label = "操作更新时间")
	private Timestamp updated_at;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public Operation(int id, String name, String description,
			Timestamp created_at, Timestamp updated_at) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.created_at = created_at;
		this.updated_at = updated_at;
	}

	public Operation(String name, String description, Timestamp created_at,
			Timestamp updated_at) {
		this.name = name;
		this.description = description;
		this.created_at = created_at;
		this.updated_at = updated_at;
	}

	public Operation() {
		super();
	}

}
