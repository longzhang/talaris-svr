/**
 * 
 */
package me.ele.talaris.deliveryorder.persistent.dao;

import java.util.List;

import me.ele.talaris.deliveryorder.persistent.eb.DeliveryOrderRecord;
import me.ele.talaris.framework.dao.BaseSpringDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author shaorongfei
 *
 */
public class DeliveryOrderRecordDao extends BaseSpringDao<DeliveryOrderRecord> {

	public DeliveryOrderRecordDao() {
		super(new BeanPropertyRowMapper<DeliveryOrderRecord>(DeliveryOrderRecord.class));
	}

	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * 增加配送记录
	 * 
	 * @param deliveryOrderRecord
	 * @return
	 */
	public int addDeliveryOrderRecord(DeliveryOrderRecord deliveryOrderRecord) {
		return insert(deliveryOrderRecord);
	};

	public DeliveryOrderRecord getExceptionDeliveryOrderRecord(long id) {
		return this.selectOneOrNull("where delivery_order_id = ? and status = -1  order by created_at desc ", id);
	}

	public List<DeliveryOrderRecord> getDeliveryOrderRecordByDeliveryOrderId(long deliveryOrderId) {
		return this.select("where delivery_order_id = ?", deliveryOrderId);

	}

	public List<DeliveryOrderRecord> getLastDeliveryOrderRecordByDeliveryOrderId(long deliveryOrderId) {
		return this.select("where delivery_order_id = ? order by created_at desc limit 1 ", deliveryOrderId);

	}

}
