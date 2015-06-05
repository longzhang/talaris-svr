package me.ele.talaris.response;

import java.math.BigDecimal;

/**
 * 每日业绩
 * 
 * @author zhengwen
 *
 */
public class Achievement {
    // 年月日
    private String date;
    // 送了多少单
    private int deliver_order_count_inoneday;
    // 收了多少钱
    private BigDecimal deliver_order_payamount_inoneday;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public BigDecimal getDeliver_order_payamount_inoneday() {
        return deliver_order_payamount_inoneday;
    }

    public void setDeliver_order_payamount_inoneday(BigDecimal deliver_order_payamount_inoneday) {
        this.deliver_order_payamount_inoneday = deliver_order_payamount_inoneday;
    }

    public int getDeliver_order_count_inoneday() {
        return deliver_order_count_inoneday;
    }

    public void setDeliver_order_count_inoneday(int deliver_order_count_inoneday) {
        this.deliver_order_count_inoneday = deliver_order_count_inoneday;
    }

    public Achievement(String date, int deliver_order_count_inoneday, BigDecimal deliver_order_payamount_inoneday) {
        super();
        this.date = date;
        this.deliver_order_count_inoneday = deliver_order_count_inoneday;
        this.deliver_order_payamount_inoneday = deliver_order_payamount_inoneday;
    }

    public Achievement() {
        super();
    }

    // public static void main(String[] args) {
    // Format format = new SimpleDateFormat("YYYY-MM-dd");
    // Date date = new Date();
    //
    // System.out.println(format.format(date));
    // }
}
