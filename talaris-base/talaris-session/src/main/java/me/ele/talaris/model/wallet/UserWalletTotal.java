package me.ele.talaris.model.wallet;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public class UserWalletTotal {

	private int user_id;
	
	private BigDecimal total_allowance;
	
	private BigDecimal paid_allowance;
	
	private BigDecimal unpaid_allowance;
	
	private Timestamp created_at;

	private Timestamp updated_at;
	
	private List<UserWalletDaily> wallet_detail_daily;

	private int daily_list_size;
	
	public int getDaily_list_size() {
		return daily_list_size;
	}

	public void setDaily_list_size(int daily_list_size) {
		this.daily_list_size = daily_list_size;
	}

	public UserWalletTotal(BigDecimal total_allowance, BigDecimal paid_allowance, BigDecimal unpaid_allowance) {
		super();
		this.total_allowance = total_allowance;
		this.paid_allowance = paid_allowance;
		this.unpaid_allowance = unpaid_allowance;
	}

	public UserWalletTotal(double total_allowance,
			double paid_allowance, double unpaid_allowance) {
		super();
		this.total_allowance = new BigDecimal(total_allowance);
		this.paid_allowance = new BigDecimal(paid_allowance);
		this.unpaid_allowance = new BigDecimal(unpaid_allowance);
	}

	public UserWalletTotal() {
		super();
		this.total_allowance = new BigDecimal(0);
		this.paid_allowance = new BigDecimal(0);
		this.unpaid_allowance = new BigDecimal(0);
	}

	public void incTotalAllowance(BigDecimal incValue) {
		total_allowance = total_allowance.add(incValue);
	}
	
	public void incPaidAllowance(BigDecimal incValue) {
		paid_allowance = paid_allowance.add(incValue);
	}
	
	public void incUnpaidAllowance(BigDecimal incValue) {
		unpaid_allowance = unpaid_allowance.add(incValue);
	}
	
	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public BigDecimal getTotal_allowance() {
		return total_allowance;
	}

	public void setTotal_allowance(BigDecimal total_allowance) {
		this.total_allowance = total_allowance;
	}

	public BigDecimal getPaid_allowance() {
		return paid_allowance;
	}

	public void setPaid_allowance(BigDecimal paid_allowance) {
		this.paid_allowance = paid_allowance;
	}

	public BigDecimal getUnpaid_allowance() {
		return unpaid_allowance;
	}

	public void setUnpaid_allowance(BigDecimal unpaid_allowance) {
		this.unpaid_allowance = unpaid_allowance;
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

	public List<UserWalletDaily> getWallet_detail_daily() {
		return wallet_detail_daily;
	}

	public void setWallet_detail_daily(List<UserWalletDaily> wallet_detail_daily) {
		this.wallet_detail_daily = wallet_detail_daily;
	}

}
