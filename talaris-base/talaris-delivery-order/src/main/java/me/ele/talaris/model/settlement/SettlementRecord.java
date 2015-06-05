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
public class SettlementRecord implements Comparable<SettlementRecord> {
	private long id;
	private Timestamp settle_time;

	private int normal_total_count;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	private BigDecimal normal_total_sum;

	public Timestamp getSettle_time() {
		return settle_time;
	}

	public void setSettle_time(Timestamp settle_time) {
		this.settle_time = settle_time;
	}

	public int getNormal_total_count() {
		return normal_total_count;
	}

	public void setNormal_total_count(int normal_total_count) {
		this.normal_total_count = normal_total_count;
	}

	public BigDecimal getNormal_total_sum() {
		return normal_total_sum;
	}

	public void setNormal_total_sum(BigDecimal normal_total_sum) {
		this.normal_total_sum = normal_total_sum;
	}

	@Override
	public int compareTo(SettlementRecord o) {
		return new Long(o.getId()).compareTo(this.getId());
	}

	/**
	 * 
	 */
	public SettlementRecord() {
		super();
	}

//	public static void main(String args[]) {
//		List<SettlementRecord> list = new ArrayList<SettlementRecord>();
//		for (int i = 0; i < 10; i++) {
//			SettlementRecord se = new SettlementRecord();
//			se.setId(i);
//			list.add(se);
//		}
//		Collections.sort(list);
//	    for (SettlementRecord settlementRecord : list) {
//			System.out.println(settlementRecord.getId());
//		}
//	}
}
