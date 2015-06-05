package me.ele.talaris.webapi.settlement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.ele.talaris.constant.Constant;
import me.ele.talaris.context.utils.ContextUtil;
import me.ele.talaris.exception.SystemException;
import me.ele.talaris.exception.UserException;
import me.ele.talaris.model.Context;
import me.ele.talaris.model.settlement.ConfirmResult;
import me.ele.talaris.model.settlement.DeliveryOrderPartInfo;
import me.ele.talaris.model.settlement.GetDeliveryOrderPartInfoFilter;
import me.ele.talaris.model.settlement.HistorySettleInfo;
import me.ele.talaris.model.settlement.SettleDetail;
import me.ele.talaris.model.settlement.TakerNoSettleInfo;
import me.ele.talaris.model.settlement.ViewDeliveryOrderInfos;
import me.ele.talaris.service.deliveryorder.impl.CommonDeliveryOrderService;
import me.ele.talaris.service.permission.IPermissionService;
import me.ele.talaris.service.settlement.IGetNoSettleInfoService;
import me.ele.talaris.service.settlement.ISettlementGetDeliveryOrderService;
import me.ele.talaris.service.settlement.ISettlementService;
import me.ele.talaris.utils.SerializeUtil;
import me.ele.talaris.web.framework.ResponseEntity;
import me.ele.talaris.web.framework.WebAPIBaseController;
import me.ele.talaris.web.log.report.InterfaceMonitor;
import me.ele.talaris.webapi.station.StationController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/webapi/")
@Transactional
public class SettlementController extends WebAPIBaseController {
	@Autowired
	IPermissionService permitionValidateService;
	@Autowired
	ISettlementService settlementService;
	@Autowired
	IGetNoSettleInfoService getNoSettleInfoService;
	@Autowired
	ISettlementGetDeliveryOrderService settlementGetDeiliveryOrderService;
	private final static Logger logger = LoggerFactory.getLogger(StationController.class);

	/**
	 * 根据条件查询配送单
	 * 
	 * @param context
	 * @param getDeliveryOrderPartInfoFilter
	 * @return
	 * @throws UserException
	 * @throws NumberFormatException
	 */
	@RequestMapping(value = "settlement/delivery_order")
	@InterfaceMonitor(interfaceName = "结算模块查看配送单", contextIndex = 0)
	public @ResponseBody ResponseEntity<ViewDeliveryOrderInfos> getDeliveryOrder(Context context,
			@ModelAttribute GetDeliveryOrderPartInfoFilter getDeliveryOrderPartInfoFilter)
			throws NumberFormatException, UserException {
		ResponseEntity<ViewDeliveryOrderInfos> responseEntity = null;
		logger.debug(getDeliveryOrderPartInfoFilter.toString());
		// 权限检查，context的用户必须是station的管理员才可以查看该station
		if (!permitionValidateService.hasRole(context, Constant.STATIONMANAGER + "," + Constant.COURIER)) {
			responseEntity = new ResponseEntity<ViewDeliveryOrderInfos>("STATION_ERROR_330", "您无权限查看对应信息", null);
			return responseEntity;
		}
		if (getDeliveryOrderPartInfoFilter.getPage_now() <= 0 || getDeliveryOrderPartInfoFilter.getPage_size() <= 0
				|| getDeliveryOrderPartInfoFilter.getPage_size() >= 100) {
			responseEntity = new ResponseEntity<ViewDeliveryOrderInfos>("STATION_ERROR_330", "请求参数错误", null);
			return responseEntity;
		}
		ViewDeliveryOrderInfos viewDeliveryOrderInfos;
		// 与前端约定，该接口被配送员自己调用，则不传Page_now和Page_size和taker_id，默认返回自己的配送单信息，
		if (getDeliveryOrderPartInfoFilter.getTaker_id() == 0 && getDeliveryOrderPartInfoFilter.getPage_size() == 1
				&& getDeliveryOrderPartInfoFilter.getPage_now() == 1) {
			viewDeliveryOrderInfos = settlementGetDeiliveryOrderService.takerGetOwnDeliveryOrder(context,
					getDeliveryOrderPartInfoFilter.getStatus(), getDeliveryOrderPartInfoFilter.getPayment_type(),
					context.getUser().getId(), getDeliveryOrderPartInfoFilter.getRst_id(),
					Integer.valueOf(getDeliveryOrderPartInfoFilter.getDetail_level()));
		} else {
			viewDeliveryOrderInfos = settlementGetDeiliveryOrderService.getDeliveryOrder(context,
					getDeliveryOrderPartInfoFilter.getTaker_id(), getDeliveryOrderPartInfoFilter.getStatus(),
					getDeliveryOrderPartInfoFilter.getPayment_type(), getDeliveryOrderPartInfoFilter.getPage_now(),
					getDeliveryOrderPartInfoFilter.getPage_size(), getDeliveryOrderPartInfoFilter.getRst_id(),
					getDeliveryOrderPartInfoFilter.getDetail_level());
		}
		List<DeliveryOrderPartInfo> deliveryOrderPartInfos = viewDeliveryOrderInfos.getDeliveryOrderList();
		if (CollectionUtils.isEmpty(deliveryOrderPartInfos)) {
			deliveryOrderPartInfos = new ArrayList<DeliveryOrderPartInfo>();
		}
		Collections.sort(deliveryOrderPartInfos);
		viewDeliveryOrderInfos.setDeliveryOrderList(deliveryOrderPartInfos);
		responseEntity = new ResponseEntity<ViewDeliveryOrderInfos>("200", "", viewDeliveryOrderInfos);
		return responseEntity;
	}

	/**
	 * 查看站点下面配送员未结账金额
	 * 
	 * @param context
	 * @param stationId
	 * @param roleID
	 * @return
	 * @throws UserException
	 */
	@RequestMapping(value = "settlement/station/{station_id}/user")
	@InterfaceMonitor(interfaceName = "结算模块查看配送员", contextIndex = 0)
	public @ResponseBody ResponseEntity<List<TakerNoSettleInfo>> viewUsers(Context context,
			@PathVariable("station_id") int stationId, @RequestParam("role_id") int roleID) throws UserException {
		ResponseEntity<List<TakerNoSettleInfo>> responseEntity = null;
		// 权限检查，context的用户必须是station的管理员才可以查看该station
		if (!permitionValidateService.hasRole(context, Constant.STATIONMANAGER)) {
			responseEntity = new ResponseEntity<List<TakerNoSettleInfo>>("STATION_ERROR_340", "您无权限查看对应信息", null);
			return responseEntity;
		}
		int stationID = ContextUtil.getMyStationIdByContext(context);
		List<TakerNoSettleInfo> takerNoSettleInfos = getNoSettleInfoService.getNoSettleInfoByStationId(context,
				stationID, roleID);
		if (CollectionUtils.isEmpty(takerNoSettleInfos)) {
			takerNoSettleInfos = new ArrayList<TakerNoSettleInfo>();
		}
		Collections.sort(takerNoSettleInfos);
		responseEntity = new ResponseEntity<List<TakerNoSettleInfo>>("200", "", takerNoSettleInfos);
		return responseEntity;
	}

	/**
	 * 获取结算详情信息
	 * 
	 * @param context
	 * @param takerId
	 * @return
	 * @throws SystemException
	 * @throws UserException
	 */
	@RequestMapping(value = "settlement/taker_summary")
	@InterfaceMonitor(interfaceName = "获取结算详情信息", contextIndex = 0)
	public @ResponseBody ResponseEntity<SettleDetail> getTakerSettleSummary(Context context,
			@RequestHeader("HTTP-ACCESS-TOKEN") String encoding, @RequestParam("taker_id") int takerId,
			@RequestParam("rst_id") int rstId) throws SystemException, UserException {
		SettleDetail settleDetail = settlementService.getSettleDetailByTakerId(context, takerId, rstId);
		logger.info("获取结算详情信息成功");
		logger.info(SerializeUtil.beanToJson(settleDetail));
		return ResponseEntity.success(settleDetail);
	}

	/**
	 * 获取历史结算信息(分页)
	 * 
	 * @param context
	 * @param takerId
	 * @param pageNow
	 * @param pageSize
	 * @return
	 * @throws UserException
	 */
	@RequestMapping(value = "settlement/history_record")
    @InterfaceMonitor(interfaceName = "获取历史结算信息(分页)", contextIndex = 0)
    public @ResponseBody ResponseEntity<HistorySettleInfo> getTakerHistorySettleInfo(Context context,
            @RequestHeader("HTTP-ACCESS-TOKEN") String encoding, @RequestParam("taker_id") int takerId,
            @RequestParam("rst_id") int rstId, @RequestParam(value = "page_now", defaultValue = "1") int pageNow,
            @RequestParam(value = "page_size", defaultValue = "10") int pageSize) throws UserException {
        HistorySettleInfo historySettleInfo = settlementService.getHistorySettleInfo(context,takerId, rstId, pageNow, pageSize);
        logger.info("获取历史结算信息(分页)成功");
        logger.info(SerializeUtil.beanToJson(historySettleInfo));
        return ResponseEntity.success(historySettleInfo);
    }

	/**
	 * 确定结算
	 * 
	 * @throws UserException
	 */
	@RequestMapping(value = "settlement/confirm_settle")
	@InterfaceMonitor(interfaceName = "确认结算", contextIndex = 0)
	public @ResponseBody ResponseEntity<ConfirmResult> confirmSettle(Context context,
			@RequestHeader("HTTP-ACCESS-TOKEN") String encoding, @RequestParam("taker_id") int takerId,
			@RequestParam("rst_id") int rstId) throws UserException {
		ConfirmResult confirmResult = settlementService.confirmSettle(context, takerId, rstId);
		logger.info("确定结算");
		logger.info(SerializeUtil.beanToJson(confirmResult));
		return ResponseEntity.success(confirmResult);
	}
}
