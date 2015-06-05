package me.ele.talaris.framework.dao;

import java.util.List;

public interface ICriteriaElement {
    public String getClause();

    public List<Object> getParameterList();
}
