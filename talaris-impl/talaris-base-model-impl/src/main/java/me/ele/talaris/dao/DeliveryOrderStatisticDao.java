/**
 * 
 */
package me.ele.talaris.dao;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.ele.talaris.model.Context;
import me.ele.talaris.response.DeliveryOrderStatistics;
import me.ele.talaris.utils.Times;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author shaorongfei
 */
public class DeliveryOrderStatisticDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private final static Logger logger = LoggerFactory.getLogger(DeliveryOrderStatisticDao.class);

	/**
	 * 根据userId和date返回配送单信息，配送员查看业绩
	 * 
	 * @return
	 */
	public List<DeliveryOrderStatistics> getDeliveryOrderByUserIdAndDate(int userId, String date, String groupBy) {
		String current_date = Times.currentYYYY_MM_DD();
		String current_dateNext = "";
		if (!current_date.equals(date)) {
			current_date = date;
		}
		current_dateNext = Times.plusDaysOnYYYY_MM_DD(Times.dateFromYYYY_MM_DD(current_date), 1);
		String sql = "select status ,payment_type ,count(1) as count ,sum(total_amount) as sum from delivery_order where created_at >= ? and created_at < ? "
				+ "and taker_id = ?  group by status,payment_type";
		final Object[] params = new Object[] { current_date, current_dateNext, userId };
		// 返回 a List of Maps, using column name as key. 每一个map代表一条记录
		List rows = this.jdbcTemplate.queryForList(sql, params);
		List<DeliveryOrderStatistics> list = new ArrayList<DeliveryOrderStatistics>();
		if (rows != null && rows.size() > 0) {
			for (int i = 0; i < rows.size(); i++) {
				Map deliveryOrderReportMap = (Map) rows.get(i);
				DeliveryOrderStatistics deliveryOrderStatistics = new DeliveryOrderStatistics();
				logger.info(deliveryOrderReportMap.toString());
				logger.info(deliveryOrderReportMap.keySet().toString());
				logger.info(deliveryOrderReportMap.values().toString());
				deliveryOrderStatistics.setStatus(Integer.parseInt(deliveryOrderReportMap.get("status").toString()));
				deliveryOrderStatistics.setPayment_type(Integer.parseInt(deliveryOrderReportMap.get("payment_type")
						.toString()));
				deliveryOrderStatistics.setCount(Integer.parseInt(deliveryOrderReportMap.get("count").toString()));
				deliveryOrderStatistics.setSum(new BigDecimal(deliveryOrderReportMap.get("sum").toString()));
				list.add(deliveryOrderStatistics);
			}
		}
		return list;

	}

	/**
	 * 根据userId和deliveryId返回配送单信息，配送员查看结算信息
	 * 
	 * @return
	 */
	public List<DeliveryOrderStatistics> getDeliveryOrderByUserIdAndDeliveryId(int userId, int eleId,
			Timestamp lastSettleTime, String groupBy) {
		String sql = "select status ,payment_type ,count(1) as count ,sum(total_amount) as sum from delivery_order where created_at > ? "
				+ "and taker_id = ? and rst_id = ? group by status,payment_type";
		final Object[] params = new Object[] { lastSettleTime, userId, eleId };
		// 返回 a List of Maps, using column name as key. 每一个map代表一条记录
		List rows = this.jdbcTemplate.queryForList(sql, params);
		List<DeliveryOrderStatistics> list = new ArrayList<DeliveryOrderStatistics>();
		if (rows != null && rows.size() > 0) {
			for (int i = 0; i < rows.size(); i++) {
				Map deliveryOrderReportMap = (Map) rows.get(i);
				DeliveryOrderStatistics deliveryOrderStatistics = new DeliveryOrderStatistics();
				logger.info(deliveryOrderReportMap.toString());
				logger.info(deliveryOrderReportMap.keySet().toString());
				logger.info(deliveryOrderReportMap.values().toString());
				deliveryOrderStatistics.setStatus(Integer.parseInt(deliveryOrderReportMap.get("status").toString()));
				deliveryOrderStatistics.setPayment_type(Integer.parseInt(deliveryOrderReportMap.get("payment_type")
						.toString()));
				deliveryOrderStatistics.setCount(Integer.parseInt(deliveryOrderReportMap.get("count").toString()));
				deliveryOrderStatistics.setSum(new BigDecimal(deliveryOrderReportMap.get("sum").toString()));
				list.add(deliveryOrderStatistics);
			}
		}
		return list;

	}

	/**
	 * 根据userId和createdate返回配送单汇总信息作为初始化结算单信息
	 * 
	 * @return
	 */
	public List<DeliveryOrderStatistics> getDeliveryOrderByUserIdAndCreateAt(int userId, String createAt, String groupBy) {
		String sql = "select status ,payment_type ,count(1) as count ,sum(total_amount) as sum from delivery_order where created_at < ? "
				+ "and taker_id = ?  and payment_type = 0 group by status,payment_type";
		final Object[] params = new Object[] { createAt, userId };
		// 返回 a List of Maps, using column name as key. 每一个map代表一条记录
		List rows = this.jdbcTemplate.queryForList(sql, params);
		List<DeliveryOrderStatistics> list = new ArrayList<DeliveryOrderStatistics>();
		if (rows != null && rows.size() > 0) {
			for (int i = 0; i < rows.size(); i++) {
				Map deliveryOrderReportMap = (Map) rows.get(i);
				DeliveryOrderStatistics deliveryOrderStatistics = new DeliveryOrderStatistics();
				logger.info(deliveryOrderReportMap.toString());
				logger.info(deliveryOrderReportMap.keySet().toString());
				logger.info(deliveryOrderReportMap.values().toString());
				deliveryOrderStatistics.setStatus(Integer.parseInt(deliveryOrderReportMap.get("status").toString()));
				deliveryOrderStatistics.setPayment_type(Integer.parseInt(deliveryOrderReportMap.get("payment_type")
						.toString()));
				deliveryOrderStatistics.setCount(Integer.parseInt(deliveryOrderReportMap.get("count").toString()));
				deliveryOrderStatistics.setSum(new BigDecimal(deliveryOrderReportMap.get("sum").toString()));
				list.add(deliveryOrderStatistics);
			}
		}
		return list;

	}

	/**
	 * 根据stationId和date返回配送单信息，汇总
	 * 
	 * @return
	 */
	public List<DeliveryOrderStatistics> getDeliveryOrderByStationIdAndDate(Context context, int stationId,
			String date, String groupBy) {
		String current_date = Times.currentYYYY_MM_DD();
		if (!current_date.equals(date)) {
			current_date = date;
		}
		String sql = "select status,payment_type,count(1) as count ,sum(total_amount) as sum from delivery_order where create_at >= ? and create_at < ? "
				+ "and stationId = ?  group by status,payment_type";
		final Object[] params = new Object[] { current_date, current_date, stationId };
		// 返回 a List of Maps, using column name as key. 每一个map代表一条记录
		List rows = this.jdbcTemplate.queryForList(sql, params);
		List<DeliveryOrderStatistics> list = new ArrayList<DeliveryOrderStatistics>();
		if (rows != null && rows.size() > 0) {
			for (int i = 0; i < rows.size(); i++) {
				Map deliveryOrderReportMap = (Map) rows.get(i);
				DeliveryOrderStatistics deliveryOrderStatistics = new DeliveryOrderStatistics();
				deliveryOrderStatistics.setStatus((Integer) deliveryOrderReportMap.get("status"));
				deliveryOrderStatistics.setPayment_type((Integer) deliveryOrderReportMap.get("payment_type"));
				deliveryOrderStatistics.setCount((Integer) deliveryOrderReportMap.get("count"));
				deliveryOrderStatistics.setSum(new BigDecimal(deliveryOrderReportMap.get("sum").toString()));
				list.add(deliveryOrderStatistics);
			}
		}
		return list;

	}
}
