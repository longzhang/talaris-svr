package me.ele.talaris.service.settlement;

import me.ele.talaris.exception.SystemException;
import me.ele.talaris.exception.UserException;
import me.ele.talaris.model.Context;
import me.ele.talaris.model.settlement.ConfirmResult;
import me.ele.talaris.model.settlement.HistorySettleInfo;
import me.ele.talaris.model.settlement.SettleDetail;

public interface ISettlementService {

	/**
	 * 获取结算详情信息(EDU一期新版工作汇总和结算详情信息)
	 * 
	 * @param takerId
	 * @return
	 * @throws UserException
	 * @throws SystemException
	 */
	public SettleDetail getSettleDetailByTakerId(Context context, int takerId, int rstId) throws SystemException,
			UserException;

	/**
	 * 返回最新历史纪录 (分页信息) 总数和总金额另外查询返回
	 * 
	 * @param takerId
	 * @return
	 * @throws UserException
	 */
	public HistorySettleInfo getHistorySettleInfo(Context context,int takerId, int rstId, int pageNow, int pageSize)
			throws UserException;

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
	public ConfirmResult confirmSettle(Context context, int takerId, int rstId) throws SystemException, UserException;

}