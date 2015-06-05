/**
 * 
 */
package me.ele.talaris.webapi.statistics;

import java.util.HashMap;
import java.util.List;

import me.ele.talaris.exception.SystemException;
import me.ele.talaris.exception.UserException;
import me.ele.talaris.model.Context;
import me.ele.talaris.response.DeliveryOrderAllWithUserReport;
import me.ele.talaris.response.DeliveryOrderStatistics;
import me.ele.talaris.response.DeliveryOrderSummary;
import me.ele.talaris.service.statistics.IStatisticsService;
import me.ele.talaris.utils.SerializeUtil;
import me.ele.talaris.web.framework.ResponseEntity;
import me.ele.talaris.web.framework.WebAPIBaseController;
import me.ele.talaris.web.log.report.InterfaceMonitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author shaorongfei
 */
@Controller
@RequestMapping("/webapi/")
public class StatisticsController extends WebAPIBaseController {

	@Autowired
	IStatisticsService statisticsService;

	private final static Logger logger = LoggerFactory.getLogger(StatisticsController.class);

	/**
	 * 当日汇总
	 * 
	 * @param context
	 * @param today
	 * @param current_user_id
	 * @param group_by
	 * @return
	 * @throws UserException
	 * @throws SystemException
	 */
	@InterfaceMonitor(interfaceName = "当日汇总", contextIndex = 0)
	@RequestMapping(value = "/delivery_order_report/", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseEntity<List<DeliveryOrderStatistics>> getDeliveryOrderReport(Context context, @RequestParam("time") String today, @RequestParam("user") Integer current_user_id,
			@RequestParam("group_by") String groupby) throws UserException, SystemException {
		List<DeliveryOrderStatistics> list = statisticsService.getDeliveryInfoByUserId(context, current_user_id, today, groupby);
		logger.info(SerializeUtil.beanToJson(list));
		return ResponseEntity.success(list);

	}

	/**
	 * 查看业绩
	 * 
	 * @param context
	 * @param today
	 * @param current_user_id
	 * @param group_by
	 * @return
	 * @throws UserException
	 * @throws SystemException
	 */
	@InterfaceMonitor(interfaceName = "查看业绩", contextIndex = 0)
	@RequestMapping(value = "/station/courier_summary/", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseEntity<DeliveryOrderSummary> getCourierSummary(Context context, @RequestParam("time") String today, @RequestParam("user") Integer current_user_id,
			@RequestParam("group_by") String groupby) throws UserException, SystemException {
		ResponseEntity<HashMap<String, Object>> rsp = ResponseEntity.success(new HashMap<String, Object>());
		List<DeliveryOrderStatistics> list = statisticsService.getDeliveryInfoByUserId(context, current_user_id, today, groupby);
		DeliveryOrderSummary summaryList = new DeliveryOrderSummary();
		if (list != null) {
			summaryList.setDeliveryOrderReport(list);
		}
		logger.info(SerializeUtil.beanToJson(summaryList));
		return ResponseEntity.success(summaryList);

	}

	/**
	 * 订单总览
	 * 
	 * @param context
	 * @param today
	 * @param current_user_id
	 * @param group_by
	 * @return
	 * @throws UserException
	 * @throws SystemException
	 */
	@InterfaceMonitor(interfaceName = "订单总览", contextIndex = 0)
	@RequestMapping(value = "/station/courier_summary_all/", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseEntity<List<DeliveryOrderAllWithUserReport>> getCourierSummaryAll(Context context, @RequestParam("time") String today, @RequestParam("station") Integer stationId,
			@RequestParam("group_by") String groupby) throws UserException, SystemException {
		List<DeliveryOrderAllWithUserReport> list = statisticsService.getDeliveryInfoByStationId(context, stationId, today, groupby);
		logger.info(SerializeUtil.beanToJson(list));
		return ResponseEntity.success(list);

	}
}
