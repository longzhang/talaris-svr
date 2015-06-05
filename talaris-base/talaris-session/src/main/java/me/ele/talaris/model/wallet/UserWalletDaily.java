package me.ele.talaris.model.wallet;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class UserWalletDaily implements Comparable<UserWalletDaily>{

	private BigDecimal allowance;
	
	private String type;
	
	private int is_paid;
	
	private Date predict_pay_date;
	
	private Date date;
	
	private Timestamp created_at;
	
	private Timestamp updated_at;

	public BigDecimal getAllowance() {
		return allowance;
	}

	public Date getPredict_pay_date() {
		return predict_pay_date;
	}

	public void setPredict_pay_date(Date predict_pay_date) {
		this.predict_pay_date = predict_pay_date;
	}

	public void setAllowance(BigDecimal allowance) {
		this.allowance = allowance;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getIs_paid() {
		return is_paid;
	}

	public void setIs_paid(int is_paid) {
		this.is_paid = is_paid;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
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

	@Override
	public int compareTo(UserWalletDaily o) {
		long current = this.getCreated_at().getTime();
		long incoming = o.getCreated_at().getTime();
		long gap = current - incoming;
		return gap < 0 ? 1 : (gap == 0 ? 0 : -1);
	}
	
}
