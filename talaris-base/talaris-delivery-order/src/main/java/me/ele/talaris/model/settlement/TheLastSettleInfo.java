/**
 * 
 */
package me.ele.talaris.model.settlement;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author shaorongfei
 *
 */
public class TheLastSettleInfo {
	private long deliveryOrderId;
	private Timestamp settleTime;
	private BigDecimal lastSettleOfflineCash;
	private int isFirstAddDeliveryMan;

	public int getIsFirstAddDeliveryMan() {
		return isFirstAddDeliveryMan;
	}

	public void setIsFirstAddDeliveryMan(int isFirstAddDeliveryMan) {
		this.isFirstAddDeliveryMan = isFirstAddDeliveryMan;
	}

	public BigDecimal getLastSettleOfflineCash() {
		return lastSettleOfflineCash;
	}

	public void setLastSettleOfflineCash(BigDecimal lastSettleOfflineCash) {
		this.lastSettleOfflineCash = lastSettleOfflineCash;
	}

	public Timestamp getSettleTime() {
		return settleTime;
	}

	public long getDeliveryOrderId() {
		return deliveryOrderId;
	}

	public void setDeliveryOrderId(long deliveryOrderId) {
		this.deliveryOrderId = deliveryOrderId;
	}

	public void setSettleTime(Timestamp settleTime) {
		this.settleTime = settleTime;
	}
}
