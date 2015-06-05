/**
 * 
 */
package me.ele.talaris.dao;

import java.util.Map;

import me.ele.talaris.exception.UserException;
import me.ele.talaris.framework.dao.BaseSpringDao;
import me.ele.talaris.model.StationRestaurant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

/**
 * @author
 *
 */
public class StationRestaurantDao extends BaseSpringDao<StationRestaurant> {
    private final static Logger logger = LoggerFactory.getLogger(StationRestaurantDao.class);

    public StationRestaurantDao() {
        super(new BeanPropertyRowMapper<StationRestaurant>(StationRestaurant.class));
    }

    /**
     * 根据关联Id查询关联表记录
     * 
     * @param id
     * @return
     */
    public StationRestaurant getStationRestaurantById(int id) {
        return this.selectOneOrNull("where id = ?", id);
    }
    
    /**
     * 根据rstId查询关联表纪录
     * @param rstId
     * @return
     */
    public StationRestaurant getStationRestaurantByRstId(int rstId){
    	return this.selectOneOrNull("where rst_id = ? and status = 1", rstId);
    }

    /**
     * 插入配送站点和餐厅关联表记录
     * 
     * @param operation
     * @return
     */
    public int insertAndRecordLog(StationRestaurant stationRestaurant) {
        int result = insert(stationRestaurant);
        logger.info("插入配送站点和餐厅关联表记录:" + result);
        return result;
    }

    /**
     * 逻辑删除关联记录
     * 
     * @param operation
     * @return
     */
    public int updateAndRecordLog(int id) {
        int result = this.jdbcTemplate.update("update delivery_order set status = 0 where id = ? ", id);
        logger.info("逻辑删除管理记录:result" + result);
        return result;
    }

    public int getEleRestaurantIdByStaionId(int station_id) {
        String sql = "select rst_id from station_restaurant where station_id = ? ";
        Map<String, Object> map = jdbcTemplate.queryForMap(sql, station_id);// queryForLong @Deprecated

        // 这里dao层如果异常直接往上抛，由service来抓取
        Integer rst_id = (Integer) map.get("rst_id");

        String eleSql = "select ele_id from restaurant where id = ? ";
        Map<String, Object> eleMap = jdbcTemplate.queryForMap(eleSql, rst_id);
        return (Integer) eleMap.get("ele_id");
    }

    public int getRestaurantIdByStaionId(int station_id) {
        String sql = "select rst_id from station_restaurant where station_id = ? ";
        Map<String, Object> map = jdbcTemplate.queryForMap(sql, station_id);// queryForLong @Deprecated

        // 这里dao层如果异常直接往上抛，由service来抓取
        Integer rst_id = (Integer) map.get("rst_id");
        return rst_id;
    }

}
