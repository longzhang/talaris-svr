/**
 * 
 */
package me.ele.talaris.dao;

import java.util.List;

import me.ele.talaris.framework.dao.BaseSpringDao;
import me.ele.talaris.model.SettlementDeliveryOrder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

/**
 * @author shaorongfei
 *
 */
public class SettlementDeliveryOrderDao extends BaseSpringDao<SettlementDeliveryOrder> {

	private final static Logger logger = LoggerFactory.getLogger(SettlementDeliveryOrderDao.class);

	public SettlementDeliveryOrderDao() {
		super(new BeanPropertyRowMapper<SettlementDeliveryOrder>(SettlementDeliveryOrder.class));
	}

	/**
	 * 增加结算配送关联信息
	 * 
	 * @param settlement
	 * @return
	 */
	public int addSettlementDelivery(SettlementDeliveryOrder settlementDeliveryOrder) {
		int result = insert(settlementDeliveryOrder);
		logger.debug("增加结算配送关联信息:" + result);
		return result;
	}

	/**
	 * 根据takerId和settlementId获取结算信息
	 * @param settlementId
	 * @param takerId
	 * @return
	 */
	public List<SettlementDeliveryOrder> getSettlementDeliveryOrderByTakerId(long settlementId, int takerId) {
		List<SettlementDeliveryOrder> result = (List<SettlementDeliveryOrder>) this.select("where taker_id = ? and settlement_id = ?", takerId, settlementId);
		logger.debug("获取结算配送关联信息:" + result);
		return result;
	}

}
