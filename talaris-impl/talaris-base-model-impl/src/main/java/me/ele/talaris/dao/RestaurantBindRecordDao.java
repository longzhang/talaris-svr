package me.ele.talaris.dao;

import me.ele.talaris.framework.dao.BaseSpringDao;
import me.ele.talaris.model.RestaurantBindRecord;

import org.springframework.jdbc.core.BeanPropertyRowMapper;

/**
 * 
 * @author Kimizhang
 *
 */

public class RestaurantBindRecordDao extends BaseSpringDao<RestaurantBindRecord> {

	public RestaurantBindRecordDao() {
		super(new BeanPropertyRowMapper<RestaurantBindRecord>(RestaurantBindRecord.class));
	}

}
