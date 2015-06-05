package me.ele.talaris.dao;

import me.ele.talaris.framework.dao.BaseSpringDao;
import me.ele.talaris.model.RestaurantIdsModule;

import org.springframework.jdbc.core.BeanPropertyRowMapper;

/**
 * 去napos拉单对应的餐厅块
 * 
 * @author zhengwen
 *
 */
public class RestaurantIdsModuleDao extends BaseSpringDao<RestaurantIdsModule> {

    public RestaurantIdsModuleDao() {
        super(new BeanPropertyRowMapper<RestaurantIdsModule>(RestaurantIdsModule.class));
    }

    /**
     * 调用该方法时候加上事务
     * 
     * @return
     */
    public RestaurantIdsModule getEarliestRestaurantIdsModule() {
        return this.selectOneOrNull("order by id desc limit 1");
    }

    // /**
    // * 开站的时候记得更新该行（关战的时候是否更新某一行暂时不做）
    // * 调用该方法时候加上事务
    // *
    // * @return
    // */
    // public RestaurantIdsModule getLatestRestaurantIdsModule() {
    // return this.selectOneOrNull("order by last_update_time desc limit 1 for update");
    // }

    /**
     * 更新一个餐厅块里的餐厅，或者是上次的拉单时间
     * 
     * 
     */
    public void insertRestaurantIdsModule(RestaurantIdsModule restaurantIdsModule) {
        String sql = "insert into channel_restaurant_module(current_module_id,rst_count_once,create_at) values(?,?,?)";
        jdbcTemplate.update(sql, restaurantIdsModule.getCurrent_module_id(), restaurantIdsModule.getRst_count_once(),
                restaurantIdsModule.getCreate_at());
    }
}
