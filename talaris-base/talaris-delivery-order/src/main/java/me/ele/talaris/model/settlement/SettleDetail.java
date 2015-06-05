/**
 * 
 */
package me.ele.talaris.model.settlement;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import me.ele.talaris.response.DeliveryOrderStatistics;

/**
 * @author shaorongfei
 *
 */
public class SettleDetail {
	private Timestamp the_last_settle_time;

	private BigDecimal the_last_settle_cash;

	private List<DeliveryOrderStatistics> deliveryOrderReport;
	
	private int isFirstAddDeliveryMan;

	public int getIsFirstAddDeliveryMan() {
		return isFirstAddDeliveryMan;
	}

	public void setIsFirstAddDeliveryMan(int isFirstAddDeliveryMan) {
		this.isFirstAddDeliveryMan = isFirstAddDeliveryMan;
	}

	public Timestamp getThe_last_settle_time() {
		return the_last_settle_time;
	}

	public void setThe_last_settle_time(Timestamp the_last_settle_time) {
		this.the_last_settle_time = the_last_settle_time;
	}

	public BigDecimal getThe_last_settle_cash() {
		return the_last_settle_cash;
	}

	public void setThe_last_settle_cash(BigDecimal the_last_settle_cash) {
		this.the_last_settle_cash = the_last_settle_cash;
	}

	public List<DeliveryOrderStatistics> getDeliveryOrderReport() {
		return deliveryOrderReport;
	}

	public void setDeliveryOrderReport(List<DeliveryOrderStatistics> deliveryOrderReport) {
		this.deliveryOrderReport = deliveryOrderReport;
	}

}
