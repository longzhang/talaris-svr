/**
 * 
 */
package me.ele.talaris.service.statistics.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.ele.talaris.dao.DeliveryOrderStatisticDao;
import me.ele.talaris.exception.ExceptionCode;
import me.ele.talaris.exception.ExceptionFactory;
import me.ele.talaris.exception.SystemException;
import me.ele.talaris.exception.UserException;
import me.ele.talaris.model.Context;
import me.ele.talaris.model.User;
import me.ele.talaris.response.DeliveryOrderAllWithUserReport;
import me.ele.talaris.response.DeliveryOrderStatistics;
import me.ele.talaris.service.station.IStationService;
import me.ele.talaris.service.statistics.IStatisticsService;
import me.ele.talaris.service.user.IUserService;
import me.ele.talaris.utils.Times;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author shaorongfei
 */
@Service
public class StatisticsService implements IStatisticsService {
	@Autowired
	DeliveryOrderStatisticDao deliveryOrderStatisticDao;

	@Autowired
	IStationService stationService;

	@Autowired
	IUserService userService;

	private final static Logger logger = LoggerFactory.getLogger(StatisticsService.class);

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
	@Override
	public List<DeliveryOrderAllWithUserReport> getDeliveryInfoByStationId(Context context, int stationId, String date,
			String groupBy) throws UserException, SystemException {
		List<DeliveryOrderAllWithUserReport> listOrderAllWithUser = new ArrayList<DeliveryOrderAllWithUserReport>();
		try {
			List<User> userList = stationService.listCourierByStation(context);// 站点管理员获取该站的所有配送员
			// List<User> userListNew = new ArrayList<User>();
			;
			// for (User user : userList) {
			// User userNew =
			// userService.getUserByUserIdNoMatterStatus(user.getId());
			// if (userNew != null) {
			// userListNew.add(userNew);
			// }
			// }

			for (int i = 0; i < userList.size(); i++) {
				List<DeliveryOrderStatistics> listOrder = this.getDeliveryInfoByUserId(context,
						userList.get(i).getId(), date, groupBy);
				DeliveryOrderAllWithUserReport orderAllWithUser = new DeliveryOrderAllWithUserReport();
				orderAllWithUser.setUser(userList.get(i));
				orderAllWithUser.setDeliveryOrderReport(listOrder);
				listOrderAllWithUser.add(orderAllWithUser);
			}
		} catch (Exception e) {
			logger.error("系统异常" + e);
			throw ExceptionFactory.newSystemException(ExceptionCode.STATION_ERROR_370, e);
		}

		return listOrderAllWithUser;

	}

	/**
	 * 查看某个配送员业绩信息
	 * 
	 * @param userId
	 * @param date
	 * @param groupBy
	 *            status paymenttype sql里做处理
	 * @return
	 */
	@Override
	public List<DeliveryOrderStatistics> getDeliveryInfoByUserId(Context context, int userId, String date,
			String groupBy) throws UserException, SystemException {
		List<DeliveryOrderStatistics> list = null;
		try {
			String dateRight = "";
			if (date.equals("today")) {
				dateRight = Times.currentYYYY_MM_DD();
			} else {
				Date dateTime = Times.dateFromYYYY_MM_DD(date);
				dateRight = Times.toYYYY_MM_DD_HHMMSS(dateTime);
			}
			list = deliveryOrderStatisticDao.getDeliveryOrderByUserIdAndDate(userId, dateRight, groupBy);
		} catch (Exception e) {
			logger.error("查看某个配送员业绩信息系统异常" + e);
			throw ExceptionFactory.newSystemException(ExceptionCode.STATION_ERROR_370, e);
		}
		return list;

	}

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
	@Override
	public List<DeliveryOrderStatistics> getSettleInfoByUserIdAndDeliveryOrderId(Context context, int userId,
			int eleId, Timestamp lastSettleTime, String groupBy) throws SystemException {
		List<DeliveryOrderStatistics> list = null;
		try {
			list = deliveryOrderStatisticDao.getDeliveryOrderByUserIdAndDeliveryId(userId, eleId, lastSettleTime,
					groupBy);
		} catch (Exception e) {
			logger.error("查看某个配送员结算信息餐到付款异常" + e);
			throw ExceptionFactory.newSystemException(ExceptionCode.STATION_ERROR_370, e);
		}
		return list;

	}

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
	@Override
	public List<DeliveryOrderStatistics> getDeliveryOrderByUserIdAndCreateAt(Context context, int userId,
			String createAt, String groupBy) throws SystemException {
		List<DeliveryOrderStatistics> list = null;
		try {
			list = deliveryOrderStatisticDao.getDeliveryOrderByUserIdAndCreateAt(userId, createAt, groupBy);
		} catch (Exception e) {
			logger.error("初始化某个配送员结算信息异常" + e);
			throw ExceptionFactory.newSystemException(ExceptionCode.STATION_ERROR_370, e);
		}
		return list;

	}

	// public static void main(String args[]) {
	// System.out.println(new BigDecimal(new String("1.0111000000101101010")));
	// }
}
