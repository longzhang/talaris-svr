package me.ele.talaris.dao;

import me.ele.talaris.framework.dao.BaseSpringDao;
import me.ele.talaris.web.log.report.InterfaceLog;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;

public class InterfaceLogDao extends BaseSpringDao<InterfaceLog> {

    public InterfaceLogDao() {
        super(new BeanPropertyRowMapper<InterfaceLog>(InterfaceLog.class));
    }

}
