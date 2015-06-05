package me.ele.talaris.model;

import java.sql.Timestamp;

import me.ele.talaris.framework.dao.Column;
import me.ele.talaris.framework.dao.Table;

@Table(name = "bank_serial_mapping")
public class BankSerialMapping {
    @Column(pk = true, auto_increase = true)
    private int id;
    
    @Column
    private String bank_name;
    
    @Column
    private String bank_serial;
    
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

	public String getBank_name() {
		return bank_name;
	}

	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}

	public String getBank_serial() {
		return bank_serial;
	}

	public void setBank_serial(String bank_serial) {
		this.bank_serial = bank_serial;
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
