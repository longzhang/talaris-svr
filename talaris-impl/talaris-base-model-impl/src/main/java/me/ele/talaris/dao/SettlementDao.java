package me.ele.talaris.dao;

import java.sql.Timestamp;
import java.util.List;

import me.ele.talaris.framework.dao.BaseSpringDao;
import me.ele.talaris.model.Settlement;
import me.ele.talaris.utils.Pair;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

/**
 * @author shaorongfei
 *
 */
public class SettlementDao extends BaseSpringDao<Settlement> {

	private final static Logger logger = LoggerFactory.getLogger(SettlementDao.class);

	public SettlementDao() {
		super(new BeanPropertyRowMapper<Settlement>(Settlement.class));
	}

	/**
	 * 增加结算信息
	 * 
	 * @param settlement
	 * @return
	 */
	public int addSettlement(Settlement settlement) {
		int result = insert(settlement);
		logger.debug("增加结算信息:" + result);
		return result;
	}

	/**
	 * 根据配送员Id查询结算单信息
	 * 
	 * @param takerId
	 *            配送员Id
	 * @return
	 */
	public List<Settlement> getSettlementListByTakerId(int takerId) {
		List<Settlement> result = this.select("where taker_id = ?", takerId);
		// logger.debug("根据配送员Id查询结算单信息:" + result);
		return result;
	}

	/**
	 * 根据配送员Id和站点Id查询结算单信息
	 * 
	 * @param takerId
	 *            配送员Id
	 * @return
	 */
	public List<Settlement> getSettlementListByTakerId(int takerId, int stationId) {
		List<Settlement> result = this.select("where taker_id = ? and station_id = ? ", takerId, stationId);
		// logger.debug("根据配送员Id查询结算单信息:" + result);
		return result;
	}

	/**
	 * 根据配送员Id查询有效结算单信息
	 * 
	 * @param takerId
	 *            配送员Id
	 * @return
	 */
	public List<Settlement> getEffectiveSettlementListByTakerId(int takerId, int stationID) {
		List<Settlement> result = this.select("where taker_id = ? and station_id = ? and status = 1", takerId,
				stationID);
		// logger.debug("根据配送员Id查询结算单信息:" + result);
		return result;
	}

	/**
	 * 根据站点Id查询结算单信息
	 * 
	 * @param stationId
	 *            站点ID
	 * @return
	 */
	public List<Settlement> getSettlementListByStationId(int stationId) {
		List<Settlement> result = this.select("where station_id = ?", stationId);
		// logger.debug("根据站点Id查询结算单信息:" + result);
		return result;
	}

	/**
	 * 查看初始化结算记录
	 * 
	 * @param stationId
	 * @param takerId
	 * @return
	 */
	public Settlement getSettlementByStationIdAndTakerId(int stationId, int takerId) {
		return this.selectOneOrNull("where station_id = ? and taker_id = ? and status = 0", stationId, takerId);
	}

	/**
	 * 查询PageSettlementList分页信息
	 * 
	 * @param takerId
	 * @param pageNum
	 * @param PageSize
	 * @param args
	 * @return
	 */
	public Pair<List<Settlement>, Long> getPageSettlementList(int takerId, int stationId, int pageNum, int PageSize) {
		String where = "where taker_id = ? and station_id = ? and status = 1";
		Pair<List<Settlement>, Long> pair;
		pair = this.selectPage(where, pageNum, PageSize, takerId, stationId);
		return pair;
	}

	/**
	 * 彻底删除配送员，之前的结算纪录status置为－1,但保留初始化纪录信息
	 * 
	 * @param stationId
	 * @param takerId
	 * @param deleteTime
	 * @return
	 */
	public int updateSettlementForDeletedMan(int stationId, int takerId, Timestamp deleteTime) {
		return this.jdbcTemplate.update(
				"update settlement set status = -1 where station_id = ? and taker_id = ? and status != 0 and created_at < ? ",
				stationId, takerId, deleteTime);

	}
}
