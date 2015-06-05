/**
 * 
 */
package me.ele.talaris.response;

import java.util.List;

import me.ele.talaris.model.User;

/**
 * @author shaorongfei
 */
public class DeliveryOrderAllWithUserReport {
	private User user;
	private List<DeliveryOrderStatistics> deliveryOrderReport;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<DeliveryOrderStatistics> getDeliveryOrderReport() {
		return deliveryOrderReport;
	}

	public void setDeliveryOrderReport(
			List<DeliveryOrderStatistics> deliveryOrderReport) {
		this.deliveryOrderReport = deliveryOrderReport;
	}

}
