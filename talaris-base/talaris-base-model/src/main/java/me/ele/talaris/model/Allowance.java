package me.ele.talaris.model;

import me.ele.talaris.framework.dao.Column;
import me.ele.talaris.framework.dao.Table;

@Table(name = "allowance")
public class Allowance {
    @Column(pk = true, auto_increase = true)
    private int id;
    // 订单来源
    @Column
    private int order_source;
    // 是否有补贴
    @Column
    private int has_allowance;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrder_source() {
        return order_source;
    }

    public void setOrder_source(int order_source) {
        this.order_source = order_source;
    }

    public int getHas_allowance() {
        return has_allowance;
    }

    public void setHas_allowance(int has_allowance) {
        this.has_allowance = has_allowance;
    }

    public Allowance(int id, int order_source, int has_allowance) {
        super();
        this.id = id;
        this.order_source = order_source;
        this.has_allowance = has_allowance;
    }

    public Allowance() {
        super();
    }

}
