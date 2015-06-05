/**
 * 
 */
package me.ele.talaris.response;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author shaorongfei
 *
 */
public class DeliveryOrderSettlement {
	private String lastSettleTime;

	public List<DeliveryOrderStatistics> getDeliveryOrderReport() {
		return deliveryOrderReport;
	}

	public String getLastSettleTime() {
		return lastSettleTime;
	}

	public void setLastSettleTime(String lastSettleTime) {
		this.lastSettleTime = lastSettleTime;
	}

	public void setDeliveryOrderReport(List<DeliveryOrderStatistics> deliveryOrderReport) {
		this.deliveryOrderReport = deliveryOrderReport;
	}

	private List<DeliveryOrderStatistics> deliveryOrderReport;

}
