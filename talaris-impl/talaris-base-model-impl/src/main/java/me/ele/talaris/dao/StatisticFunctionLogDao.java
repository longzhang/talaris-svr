/**
 * 
 */
package me.ele.talaris.dao;

import java.sql.Timestamp;
import java.util.List;

import me.ele.talaris.framework.dao.BaseSpringDao;
import me.ele.talaris.model.StatisticFunctionLog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author shaorongfei
 *
 */
public class StatisticFunctionLogDao extends BaseSpringDao<StatisticFunctionLog> {
	public StatisticFunctionLogDao() {
		super(new BeanPropertyRowMapper<StatisticFunctionLog>(StatisticFunctionLog.class));
	}

	private final static Logger logger = LoggerFactory.getLogger(StatisticFunctionLogDao.class);

	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * 根据统计日期查询功能统计日志
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<StatisticFunctionLog> getStatisticFunctionLogList(Timestamp startTime, Timestamp endTime) {
		List<StatisticFunctionLog> list = null;
		list = this.select("where statistic_date >= ? and statistic_date < ?", startTime, endTime);
		if (list != null && list.size() > 0) {
			logger.debug("根据统计日期查询功能统计日志:" + list);
		}
		return list;
	}

	/**
	 * 插入一条功能统计日志记录
	 * 
	 * @param operation
	 * @return
	 */
	public int insertAndRecordLog(StatisticFunctionLog statisticFunctionLog) {
		int result = insert(statisticFunctionLog);
		logger.debug("插入功能统计日志记录:" + result);
		return result;
	}

	/**
	 * 修改功能统计日志记录
	 * 
	 * @param operation
	 * @return
	 */
	public int updateAndRecordLog(StatisticFunctionLog statisticFunctionLog) {
		int result = update(statisticFunctionLog);
		logger.debug("更新功能统计日志记录:result" + result);
		return result;
	}

}
