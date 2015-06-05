/**
 * 
 */
package me.ele.talaris.dao;

import java.util.List;

import me.ele.talaris.framework.dao.BaseSpringDao;
import me.ele.talaris.model.Restaurant;
import me.ele.talaris.model.Station;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

/**
 * @author 
 *
 */
public class StationDao extends BaseSpringDao<Station> {
    public static final Logger LOGGER = LoggerFactory.getLogger(RestaurantDao.class);

    public StationDao() {
//        super(new BeanPropertyRowMapper<Station>(Station.class));
        super(new BeanPropertyRowMapper<Station>(Station.class));
        String[] columns = this.sql_allColumnString.split(",");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < columns.length; i++) {
            String column = columns[i];
            if (i == 0)
                sb.append(this.tableDescriptor.getTableName() + "." + column);
            else
                sb.append(", " + this.tableDescriptor.getTableName() + "." + column);
        }
        this.sql_selectAllColumnsClause = String.format("select %s from %s ", new Object[] { sb.toString(),
                this.tableDescriptor.getTableName() });
        this.sql_allColumnString = sb.toString();
    }

    /**
     * 增加一个站
     * 
     * @param addStation
     * @return
     */
    public int addStation(Station station) {
        return insert(station);
    };

    /**
     * 查看一个站 根据老板的手机号查看一个站的信息
     * 
     * @param getStation
     * @return
     */
    public Station getStationByMobile(Long mobile) {
        return this.selectOneOrNull("where manager_mobile = ? and status = 1", mobile);
    }

    /**
     * 查看站List 根据老板的手机号查看站List
     * 
     * @param mobile
     * @return
     */
    public List<Station> getStationListByMobile(Long mobile) {
        return this.select("where manager_mobile = ? and status = 1", mobile);
    }

    public Station getStationByPk(int pk) {
        return this.selectOneOrNull("where id = ?", pk);
    }

    /**
     * 更新站的信息
     * 
     * @return
     */
    public int updateStation(String stationName, int stationId) {
        int result = this.jdbcTemplate.update("update station set name = ? where id = ? ", stationName, stationId);
        return result;
    }

    /**
     * 删除一个站(逻辑删除)
     * 
     * @param status
     * @return
     */
    public int deleteStation(int status, int stationId) {
        int result = this.jdbcTemplate.update("update station set status = ? where id = ? ", status, stationId);
        return result;
    }
    
    /**
     * 通过餐厅老板手机号查询站点（不判断status）
     * 
     * @param mobile
     * @return
     */
    public Station getStationByManagerMobile(long mobile) {
    	return this.selectOneOrNull("where manager_mobile = ?", mobile);
    }

    public Station getStationByRestaurantId(int rstId){
        StringBuilder sb = new StringBuilder();
        sb.append(", station_restaurant sr ");
        sb.append("where " + this.tableDescriptor.getTableName() + ".id = sr.station_id ");
        sb.append("  and sr.rst_id = ?");
        String query = sb.toString();

        return this.selectOneOrNull(query, rstId);
    }
}
