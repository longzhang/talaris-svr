package me.ele.talaris.response;

import java.math.BigDecimal;

public class DeliveryOrderStatistics {
	private int status;
	private int payment_type;
	private int count;
	private BigDecimal sum;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public BigDecimal getSum() {
		return sum;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getPayment_type() {
		return payment_type;
	}

	public void setPayment_type(int payment_type) {
		this.payment_type = payment_type;
	}

	public void setSum(BigDecimal sum) {
		this.sum = sum;
	}

	public DeliveryOrderStatistics() {
		super();
	}

	public DeliveryOrderStatistics(int status, int payment_type, int count, BigDecimal sum) {
		super();
		this.status = status;
		this.payment_type = payment_type;
		this.count = count;
		this.sum = sum;
	}

}
