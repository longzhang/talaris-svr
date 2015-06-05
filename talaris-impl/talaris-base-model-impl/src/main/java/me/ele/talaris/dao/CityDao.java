/**
 * 
 */
package me.ele.talaris.dao;

import me.ele.talaris.framework.dao.BaseSpringDao;
import me.ele.talaris.model.City;

import org.springframework.jdbc.core.BeanPropertyRowMapper;

/**
 * @author kimizhang
 *
 */
public class CityDao extends BaseSpringDao<City> {

	public CityDao() {
		super(new BeanPropertyRowMapper<City>(City.class));
	}

	public int getCityIdByAreaCode(String areaCode) {
		City city = this.selectOneOrNull("where area_code = ?", areaCode);
		if (city != null) {
			return city.getId();
		} else {
			return 0;
		}
	}

}