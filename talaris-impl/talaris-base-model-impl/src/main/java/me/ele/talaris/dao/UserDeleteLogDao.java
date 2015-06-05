package me.ele.talaris.dao;

import java.util.List;

import me.ele.talaris.framework.dao.BaseSpringDao;
import me.ele.talaris.model.UserDeleteLog;

import org.springframework.jdbc.core.BeanPropertyRowMapper;

public class UserDeleteLogDao extends BaseSpringDao<UserDeleteLog> {

    public UserDeleteLogDao() {
        super(new BeanPropertyRowMapper<UserDeleteLog>(UserDeleteLog.class));
    }

    public List<UserDeleteLog> getUserDeleteLogByMobile(long mobile) {
        return this.select("where origin_mobile = ? order by created_at desc", mobile);
    }

}
