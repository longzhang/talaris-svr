package me.ele.talaris.dao;

import java.util.List;

import me.ele.talaris.framework.dao.BaseSpringDao;
import me.ele.talaris.model.Allowance;

import org.springframework.jdbc.core.BeanPropertyRowMapper;

public class AllowanceDao extends BaseSpringDao<Allowance> {

    public AllowanceDao() {
        super(new BeanPropertyRowMapper<Allowance>(Allowance.class));
    }

    public List<Allowance> getAll() {
        return this.select("where 1 = 1");
    }

}
