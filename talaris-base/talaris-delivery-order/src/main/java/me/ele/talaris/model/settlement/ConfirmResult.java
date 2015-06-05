/**
 * 
 */
package me.ele.talaris.model.settlement;

/**
 * @author shaorongfei
 *
 */
public class ConfirmResult {
	private int taker_id;
	private int settle_result;

	public int getTaker_id() {
		return taker_id;
	}

	public void setTaker_id(int taker_id) {
		this.taker_id = taker_id;
	}

	public int getSettle_result() {
		return settle_result;
	}

	public void setSettle_result(int settle_result) {
		this.settle_result = settle_result;
	}

}
