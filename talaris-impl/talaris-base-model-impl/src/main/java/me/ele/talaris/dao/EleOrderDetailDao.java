/**
 * 
 */
package me.ele.talaris.dao;

import me.ele.talaris.framework.dao.BaseSpringDao;
import me.ele.talaris.model.EleOrderDetail;

import org.springframework.jdbc.core.BeanPropertyRowMapper;

/**
 * @author shaorongfei
 *
 */
public class EleOrderDetailDao extends BaseSpringDao<EleOrderDetail> {

	public EleOrderDetailDao() {
		super(new BeanPropertyRowMapper<EleOrderDetail>(EleOrderDetail.class));
	}

	public EleOrderDetail getEleOrderByEleOrderId(long EleOrderId) {
		return this.selectOneOrNull("where ele_order_id = ?", EleOrderId);
	}

}
