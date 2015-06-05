package me.ele.talaris.service.settlement.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.ele.talaris.deliveryorder.persistent.dao.DeliveryOrderDao;
import me.ele.talaris.dao.RestaurantDao;
import me.ele.talaris.dao.SettlementDao;
import me.ele.talaris.dao.SettlementDeliveryOrderDao;
import me.ele.talaris.dao.StationRestaurantDao;
import me.ele.talaris.dao.UserDao;
import me.ele.talaris.dao.UserStationRoleDao;
import me.ele.talaris.exception.ExceptionCode;
import me.ele.talaris.exception.ExceptionFactory;
import me.ele.talaris.exception.SystemException;
import me.ele.talaris.exception.UserException;
import me.ele.talaris.model.Context;
import me.ele.talaris.model.Restaurant;
import me.ele.talaris.model.Settlement;
import me.ele.talaris.model.StationRestaurant;
import me.ele.talaris.model.settlement.ConfirmResult;
import me.ele.talaris.model.settlement.HistorySettleInfo;
import me.ele.talaris.model.settlement.SettleDetail;
import me.ele.talaris.model.settlement.SettlementRecord;
import me.ele.talaris.model.settlement.TheLastSettleInfo;
import me.ele.talaris.response.DeliveryOrderStatistics;
import me.ele.talaris.service.deliveryorder.impl.DeliveryOrderComponent;
import me.ele.talaris.service.permission.IPermissionService;
import me.ele.talaris.service.settlement.ISettlementService;
import me.ele.talaris.service.statistics.IStatisticsService;
import me.ele.talaris.utils.Pair;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * 获取结算详情，返回历史纪录，确认结算
 * 
 * @author shaorongfei
 *
 */
@Service
public class SettlementService implements ISettlementService {
	@Autowired
	DeliveryOrderDao deliveryOrderDao;
	@Autowired
	UserStationRoleDao userStationRoleDao;
	@Autowired
	IPermissionService permitionValidateService;
	@Autowired
	UserDao userDao;
	@Autowired
	IStatisticsService statisticsService;
	@Autowired
	SettlementDao settlementDao;
	@Autowired
	SettlementDeliveryOrderDao settlementDeliveryOrderDao;
	@Autowired
	DeliveryOrderComponent deliveryOrderComponent;
	@Autowired
	SettlementComponent settlementComponent;
	@Autowired
	StationRestaurantDao stationRestaurantDao;
	@Autowired
	RestaurantDao restaurantDao;
	private final static Logger logger = LoggerFactory.getLogger(SettlementService.class);

	/**
	 * 获取结算详情信息(EDU一期新版工作汇总和结算详情信息)
	 * 
	 * @param takerId
	 * @return
	 * @throws UserException
	 * @throws SystemException
	 */
	@Override
	@Transactional(isolation = Isolation.REPEATABLE_READ)
	public SettleDetail getSettleDetailByTakerId(Context context, int takerId, int eleId) throws SystemException,
			UserException {
		// 简单校验餐厅是否合法
		settlementComponent.isRestaurantLegal(context, eleId);
		SettleDetail settleDetail = new SettleDetail();
		try {
			TheLastSettleInfo theLastSettleInfo = settlementComponent.getTheLastSettleTimeByTakerId(takerId, context,
					eleId);
			// 系统自动标记异常
			logger.info("上次结配送单号:{}, 上次结算时间{}", theLastSettleInfo.getDeliveryOrderId(),
					theLastSettleInfo.getSettleTime());
			List<DeliveryOrderStatistics> settleInfoList = new ArrayList<DeliveryOrderStatistics>();
			settleInfoList = statisticsService.getSettleInfoByUserIdAndDeliveryOrderId(context, takerId, eleId,
					theLastSettleInfo.getSettleTime(), "groupBy");
			List<Settlement> list = settlementDao.getSettlementListByTakerId(takerId);
			// 有且只有一条初始化结算
			if (isFirstInit(list)) {
				settleDetail.setThe_last_settle_cash(new BigDecimal(0));
				settleDetail.setThe_last_settle_time(new Timestamp(0l));
				settleDetail.setDeliveryOrderReport(settleInfoList);
			} else {
				settleDetail.setThe_last_settle_cash(theLastSettleInfo.getLastSettleOfflineCash());
				settleDetail.setThe_last_settle_time(theLastSettleInfo.getSettleTime());
				settleDetail.setDeliveryOrderReport(settleInfoList);
			}
			if (theLastSettleInfo.getIsFirstAddDeliveryMan() == 1) {
				settleDetail.setIsFirstAddDeliveryMan(1);
			}
		} catch (Exception e) {
			throw ExceptionFactory.newUserException(ExceptionCode.SETTLEMENT_ERROR_130.getCode(), "获取结算详情信息(工作汇总)异常");
		}

		return settleDetail;
	}

	/**
	 * 有且只有一条初始化结算纪录
	 * 
	 * @param list
	 * @return
	 */
	private boolean isFirstInit(List<Settlement> list) {
		return (!CollectionUtils.isEmpty(list) && list.size() == 1 && list.get(0).getStatus() == 0);
	}

	/**
	 * 返回最新历史纪录 (分页信息) 总数和总金额另外查询返回
	 * 
	 * @param takerId
	 * @return
	 * @throws UserException
	 */
	@Override
	public HistorySettleInfo getHistorySettleInfo(Context context, int takerId, int rstId, int pageNow, int pageSize)
			throws UserException {
		// String date = Times.currentYYYY_MM_DD();
		// 简单校验餐厅是否合法
		settlementComponent.isRestaurantLegal(context, rstId);
		Restaurant restaurant = restaurantDao.getRestaurantByEleId(rstId);
		HistorySettleInfo historySettleInfo = new HistorySettleInfo();
		try {
			int stationId = 0;
			StationRestaurant stationRestaurant = stationRestaurantDao.getStationRestaurantByRstId(restaurant.getId());
			stationId = stationRestaurant != null ? stationRestaurant.getStation_id() : 0;
			List<SettlementRecord> settlementRecordlist = new ArrayList<SettlementRecord>();
			// 获取餐到付款总金额和餐到付款总数量的集合
			List<Settlement> list = settlementDao.getEffectiveSettlementListByTakerId(takerId, stationId);
			Pair<List<Settlement>, Long> pair = settlementDao.getPageSettlementList(takerId, stationId, pageNow,
					pageSize);
			logger.info("历史分页数量{}", pair.first.size());
			for (Settlement settlement : pair.first) {
				SettlementRecord record = new SettlementRecord();
				record.setNormal_total_count(settlement.getOffline_total_count());// 餐到付款数量(1.0.6版本包含异常订单的餐到付款数量)
				record.setNormal_total_sum(settlement.getOffline_total_money());// 餐到付款金额(1.0.6版本包含异常订单的餐到付款金额)
				record.setSettle_time(settlement.getCreated_at());// 结算时间
				record.setId(settlement.getId());
				settlementRecordlist.add(record);
			}
			// 倒序排列
			Collections.sort(settlementRecordlist);
			historySettleInfo.setSettlementRecord(settlementRecordlist);
			BigDecimal initOfflineCash = new BigDecimal("0");
			int initOfflineCount = 0;
			historySettleInfo.setSettled_count(list.size());
			for (Settlement settlement : list) {
				//只是在线支付也可以结算，所有存在getOffline_total_count()=0的情况，要判断
				if (settlement.getOffline_total_count() > 0) {
					initOfflineCash = initOfflineCash.add(settlement.getOffline_total_money());
				}
				initOfflineCount += 1;
			}
			// 金额和数量
			historySettleInfo.setSettled_cash(initOfflineCash);
			historySettleInfo.setSettled_count(initOfflineCount);
		} catch (Exception e) {
			throw ExceptionFactory.newUserException(ExceptionCode.SETTLEMENT_ERROR_130.getCode(), "返回历史结算纪录异常");
		}
		return historySettleInfo;
	}

	/**
	 *
	 * 确认结算 写入结算表和结算关联表
	 * 
	 * @return
	 * @param takerId
	 * @param operatorId
	 * @return
	 * @throws UserException
	 */
	@Override
	public ConfirmResult confirmSettle(Context context, int takerId, int rstId) throws SystemException, UserException {
		// 简单校验餐厅是否合法
		settlementComponent.isRestaurantLegal(context, rstId);
		ConfirmResult confirmResult = new ConfirmResult();
		confirmResult.setTaker_id(takerId);
		try {
			// 获取配送员最后结算配送单编号和时间
			TheLastSettleInfo theLastSettleInfo = settlementComponent.getTheLastSettleTimeByTakerId(takerId, context,
					rstId);
			// 查看某个配送员结算信息，限定为餐到付款
			List<DeliveryOrderStatistics> settleInfoList = statisticsService.getSettleInfoByUserIdAndDeliveryOrderId(
					context, takerId, rstId, theLastSettleInfo.getSettleTime(), "groupBy");
			int stationId = 0;
			Restaurant restaurant = restaurantDao.getRestaurantByEleId(rstId);
			StationRestaurant stationRestaurant = stationRestaurantDao.getStationRestaurantByRstId(restaurant.getId());
			stationId = stationRestaurant != null ? stationRestaurant.getStation_id() : 0;
			settlementComponent.writeSettlemenRelatedtInfo(context, null, settleInfoList, takerId, stationId,
					theLastSettleInfo);
		} catch (Exception e) {
			if (e.toString().contains("存在配送中订单，请置为送达后再结算")) {
				throw ExceptionFactory.newSystemException(ExceptionCode.SETTLEMENT_ERROR_130.getCode(),
						"存在配送中订单，请置为送达后再结算");
			}
			if (e.toString().contains("此配送员无可结账信息，无需结账")) {
				throw ExceptionFactory.newSystemException(ExceptionCode.SETTLEMENT_ERROR_130.getCode(),
						"此配送员无可结账信息，无需结账");
			}
			confirmResult.setSettle_result(0);
			throw ExceptionFactory.newSystemException(ExceptionCode.SETTLEMENT_ERROR_130.getCode(), "结算异常");
		}
		confirmResult.setSettle_result(1);
		return confirmResult;
	}

}
