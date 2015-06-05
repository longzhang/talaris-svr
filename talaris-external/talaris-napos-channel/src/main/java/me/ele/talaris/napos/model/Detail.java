package me.ele.talaris.napos.model;

import java.math.BigDecimal;
import java.util.List;

public class Detail {
    private BigDecimal total;
    private List<Group> groups;

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public Detail() {
        super();
    }

}
