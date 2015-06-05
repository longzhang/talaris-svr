/**
 * 
 */
package me.ele.talaris.response;

import java.util.List;

/**
 * @author shaorongfei
 *
 */
public class DeliveryOrderSummary {
	private List<DeliveryOrderStatistics> deliveryOrderReport;
	
	public List<DeliveryOrderStatistics> getDeliveryOrderReport() {
		return deliveryOrderReport;
	}

	public void setDeliveryOrderReport(List<DeliveryOrderStatistics> deliveryOrderReport) {
		this.deliveryOrderReport = deliveryOrderReport;
	}



}
