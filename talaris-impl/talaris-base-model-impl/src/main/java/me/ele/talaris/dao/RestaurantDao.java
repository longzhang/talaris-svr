/**
 * 
 */
package me.ele.talaris.dao;

import java.util.List;

import me.ele.talaris.framework.dao.BaseSpringDao;
import me.ele.talaris.model.Restaurant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

/**
 * @author shaorongfei
 *
 */
public class RestaurantDao extends BaseSpringDao<Restaurant> {

    public static final Logger logger = LoggerFactory.getLogger(RestaurantDao.class);

    public RestaurantDao() {
//        super(new BeanPropertyRowMapper<Restaurant>(Restaurant.class));
        super(new BeanPropertyRowMapper<Restaurant>(Restaurant.class));
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
     * 查询餐厅信息
     * 
     * @param operation
     * @return
     */
    public Restaurant getRestaurantByPk(int restaurantId) {
        return this.selectOneOrNull("where id = ?", restaurantId);
    }

    /**
     * 通过eleId查询餐厅信息
     * 
     * @param operation
     * @return
     */
    public Restaurant getRestaurantByEleId(int eleId) {
        return this.selectOneOrNull("where ele_id = ?", eleId);
    }

    /**
     * 一个手机绑定一个餐厅 通过手机号码获取一个餐厅id
     * 
     * @param mobile
     * @return Restaurant实体
     */
    public Restaurant getRestaurantByMobile(long mobile) {
        return this.selectOneOrNull("where manager_mobile = ?", mobile);
    }


    /**
     * 查询餐厅总数
     * 
     * @return
     */
    public long selectRstIdCount() {
        return this.count("where  1 = 1 order by ele_id");
    }

    public List<Integer> getRstModule(int start, int count) {
        String sql = "select ele_id from restaurant order by ele_id limit ? ,? ";
        List<Integer> result = jdbcTemplate.queryForList(sql, new Object[] { start, count }, Integer.class);
        return result;
    }

    /**
     * 插入新的餐廳
     * 
     * @param mobile
     * @return 餐厅Id
     */
    public int insertRestaurantInfo(Restaurant restaurant) {
        return insert(restaurant);
    }

    /**
     * 根据配送员id，查询和他关联的餐厅列表
     *
     * @param delivererId
     * @return
     */
    public List<Restaurant> getRetailerListByDelivererId(int delivererId) {
        StringBuilder sb = new StringBuilder();
        sb.append(", retailer_deliverer_mapping rdm ");
        sb.append("where " + this.tableDescriptor.getTableName() + ".id = rdm.retailer_id ");
        sb.append("  and rdm.deliverer_id = ?");
        String query = sb.toString();

        return this.select(query, delivererId);
    }
}
