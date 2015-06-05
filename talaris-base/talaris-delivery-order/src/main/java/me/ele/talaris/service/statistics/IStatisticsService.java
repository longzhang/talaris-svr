package me.ele.talaris.service.statistics;

import java.sql.Timestamp;
import java.util.List;

import me.ele.talaris.exception.SystemException;
import me.ele.talaris.exception.UserException;
import me.ele.talaris.model.Context;
import me.ele.talaris.response.DeliveryOrderAllWithUserReport;
import me.ele.talaris.response.DeliveryOrderStatistics;

public interface IStatisticsService {

	/**
	 * 当日汇总配送站下所有配送员业绩信息 1.通过stationId查所有配送员；2.通过配送员再查个人业绩 3.汇总
	 * 
	 * @param stationId
	 * @param userId
	 * @param date
	 * @param groupBy
	 *            status paymenttype sql里做处理
	 * @return
	 */
	public List<DeliveryOrderAllWithUserReport> getDeliveryInfoByStationId(
			Context context, int stationId, String date, String groupBy)
			throws UserException, SystemException;

	/**
	 * 查看某个配送员业绩信息
	 * 
	 * @param userId
	 * @param date
	 * @param groupBy
	 *            status paymenttype sql里做处理
	 * @return
	 */
	public List<DeliveryOrderStatistics> getDeliveryInfoByUserId(
			Context context, int userId, String date, String groupBy)
			throws UserException, SystemException;

	/**
	 * 查看某个配送员结算信息,餐到付款、在线支付
	 * 
	 * @param context
	 * @param userId
	 * @param date
	 * @param groupBy
	 * @return
	 * @throws UserException
	 * @throws SystemException
	 */
	public List<DeliveryOrderStatistics> getSettleInfoByUserIdAndDeliveryOrderId(
			Context context, int userId,int rstId, Timestamp lastSettleTime,
			String groupBy) throws SystemException;

	/**
	 * 初始化某个配送员结算信息从当天零点时间往后推
	 * 
	 * @param context
	 * @param userId
	 * @param deliveryOrderId
	 * @param groupBy
	 * @return
	 * @throws SystemException
	 */
	public List<DeliveryOrderStatistics> getDeliveryOrderByUserIdAndCreateAt(
			Context context, int userId, String createAt, String groupBy)
			throws SystemException;

}