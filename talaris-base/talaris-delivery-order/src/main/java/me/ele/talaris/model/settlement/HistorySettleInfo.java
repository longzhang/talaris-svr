/**
 * 
 */
package me.ele.talaris.model.settlement;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author shaorongfei
 *
 */
public class HistorySettleInfo {
	private List<SettlementRecord> settlementRecord;

	private int settled_count;

	private BigDecimal settled_cash;

	public List<SettlementRecord> getSettlementRecord() {
		return settlementRecord;
	}

	public void setSettlementRecord(List<SettlementRecord> settlementRecord) {
		this.settlementRecord = settlementRecord;
	}

	public int getSettled_count() {
		return settled_count;
	}

	public void setSettled_count(int settled_count) {
		this.settled_count = settled_count;
	}

	public BigDecimal getSettled_cash() {
		return settled_cash;
	}

	public void setSettled_cash(BigDecimal settled_cash) {
		this.settled_cash = settled_cash;
	}

}
