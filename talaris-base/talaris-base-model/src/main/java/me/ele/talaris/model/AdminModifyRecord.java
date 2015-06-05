/**
 * 
 */
package me.ele.talaris.model;

import java.sql.Timestamp;

import me.ele.talaris.framework.dao.Column;
import me.ele.talaris.framework.dao.Table;

/**
 * @author shaorongfei
 *
 */
@Table(name = "admin_modify_record")
public class AdminModifyRecord {
	public AdminModifyRecord() {
		super();
	}

	@Column(pk = true, auto_increase = true)
	private long id;
	@Column
	private long user_id;
	@Column
	private long old_mobile;
	@Column
	private long new_mobile;
	@Column
	private Timestamp created_at;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	public long getOld_mobile() {
		return old_mobile;
	}

	public void setOld_mobile(long old_mobile) {
		this.old_mobile = old_mobile;
	}

	public long getNew_mobile() {
		return new_mobile;
	}

	public void setNew_mobile(long new_mobile) {
		this.new_mobile = new_mobile;
	}

	public Timestamp getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Timestamp created_at) {
		this.created_at = created_at;
	}
}
