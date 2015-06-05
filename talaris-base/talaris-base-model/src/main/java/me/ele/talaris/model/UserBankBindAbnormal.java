package me.ele.talaris.model;

import java.sql.Timestamp;

import me.ele.talaris.framework.dao.Column;
import me.ele.talaris.framework.dao.Table;

@Table(name = "user_bank_bind_abnormal")
public class UserBankBindAbnormal {
    @Column(pk = true, auto_increase = true)
    private int id;

    @Column
    private String trade_no;
     
    @Column
    private String partner_id;
    
    @Column
    private int is_processed;
    
    @Column
    private Timestamp created_at;

    @Column
    private Timestamp updated_at;

	public UserBankBindAbnormal(String trade_no, String partner_id,
			int is_processed, Timestamp created_at, Timestamp updated_at) {
		super();
		this.trade_no = trade_no;
		this.partner_id = partner_id;
		this.is_processed = is_processed;
		this.created_at = created_at;
		this.updated_at = updated_at;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTrade_no() {
		return trade_no;
	}

	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}

	public String getPartner_id() {
		return partner_id;
	}

	public void setPartner_id(String partner_id) {
		this.partner_id = partner_id;
	}

	public int getIs_processed() {
		return is_processed;
	}

	public void setIs_processed(int is_processed) {
		this.is_processed = is_processed;
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
