package me.ele.talaris.model;

import java.sql.Timestamp;

import me.ele.talaris.framework.dao.Column;
import me.ele.talaris.framework.dao.Table;

@Table(name = "user_bank_info")
public class UserBankInfo {
    @Column(pk = true, auto_increase = true)
    private int id;
    
    @Column
    private int user_id;
    
    @Column
    private String user_name;

    @Column
    private int bank_id;
    
    @Column
    private String bank_account;
    
    @Column
    private int is_bind;
    
    @Column
    private int is_active;
    
    @Column
    private Timestamp created_at;

    @Column
    private Timestamp updated_at;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public int getBank_id() {
		return bank_id;
	}

	public void setBank_id(int bank_id) {
		this.bank_id = bank_id;
	}

	public String getBank_account() {
		return bank_account;
	}

	public void setBank_account(String bank_account) {
		this.bank_account = bank_account;
	}

	public int getIs_bind() {
		return is_bind;
	}

	public void setIs_bind(int is_bind) {
		this.is_bind = is_bind;
	}

	public int getIs_active() {
		return is_active;
	}

	public void setIs_active(int is_active) {
		this.is_active = is_active;
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
