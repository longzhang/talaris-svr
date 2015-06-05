/**
 * 
 */
package me.ele.talaris.model.settlement;

import java.math.BigDecimal;

/**
 * 
 * @author zhengwen
 *
 */
public class TakerNoSettleInfo implements Comparable<TakerNoSettleInfo> {
    private int id;
    private String name;
    private BigDecimal no_settle_cash;
    private long mobile;
    private int is_effective;

    public TakerNoSettleInfo(int id, String name, BigDecimal no_settle_cash, long mobile, int is_effective) {
        super();
        this.id = id;
        this.name = name;
        this.no_settle_cash = no_settle_cash;
        this.mobile = mobile;
        this.is_effective = is_effective;
    }

    public int getIs_effective() {
        return is_effective;
    }

    public void setIs_effective(int is_effective) {
        this.is_effective = is_effective;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getNo_settle_cash() {
        return no_settle_cash;
    }

    public void setNo_settle_cash(BigDecimal no_settle_cash) {
        this.no_settle_cash = no_settle_cash;
    }

    public long getMobile() {
        return mobile;
    }

    public void setMobile(long mobile) {
        this.mobile = mobile;
    }

    public TakerNoSettleInfo() {
        super();
    }

    @Override
    public int compareTo(TakerNoSettleInfo o) {
        return o.getNo_settle_cash().compareTo(this.getNo_settle_cash());
    }
}
