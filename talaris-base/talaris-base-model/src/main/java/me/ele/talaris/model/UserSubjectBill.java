package me.ele.talaris.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import me.ele.talaris.framework.dao.Column;
import me.ele.talaris.framework.dao.Table;

@Table(name = "user_subject_bill")
public class UserSubjectBill {
    @Column(pk = true, auto_increase = true)
    private long id;
    
    @Column
    private int user_id;
    
    @Column
    private String subject_code;

    @Column
    private String subject_type;
    
    @Column
    private BigDecimal amount;
    
    @Column
    private int amount_type;
    
    @Column
    private int user_bill_id;
    
    @Column
    private Date clear_date;
    
    @Column
    private int activity_id;
    
    @Column
    private String activity_name;
    
    @Column
    private long ele_finance_bill_detail_id;
    
    @Column
    private long breakpoint_id;
    
    @Column
    private Timestamp created_at;
    
    @Column
    private int is_active;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getSubject_code() {
		return subject_code;
	}

	public void setSubject_code(String subject_code) {
		this.subject_code = subject_code;
	}

	public String getSubject_type() {
		return subject_type;
	}

	public void setSubject_type(String subject_type) {
		this.subject_type = subject_type;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public int getAmount_type() {
		return amount_type;
	}

	public void setAmount_type(int amount_type) {
		this.amount_type = amount_type;
	}

	public int getUser_bill_id() {
		return user_bill_id;
	}

	public void setUser_bill_id(int user_bill_id) {
		this.user_bill_id = user_bill_id;
	}

	public Date getClear_date() {
		return clear_date;
	}

	public void setClear_date(Date clear_date) {
		this.clear_date = clear_date;
	}

	public int getActivity_id() {
		return activity_id;
	}

	public void setActivity_id(int activity_id) {
		this.activity_id = activity_id;
	}

	public String getActivity_name() {
		return activity_name;
	}

	public void setActivity_name(String activity_name) {
		this.activity_name = activity_name;
	}

	public long getEle_finance_bill_detail_id() {
		return ele_finance_bill_detail_id;
	}

	public void setEle_finance_bill_detail_id(long ele_finance_bill_detail_id) {
		this.ele_finance_bill_detail_id = ele_finance_bill_detail_id;
	}

	public long getBreakpoint_id() {
		return breakpoint_id;
	}

	public void setBreakpoint_id(long breakpoint_id) {
		this.breakpoint_id = breakpoint_id;
	}

	public Timestamp getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Timestamp created_at) {
		this.created_at = created_at;
	}

	public int getIs_active() {
		return is_active;
	}

	public void setIs_active(int is_active) {
		this.is_active = is_active;
	}

	public UserSubjectBill() {
		super();
	}
	
}