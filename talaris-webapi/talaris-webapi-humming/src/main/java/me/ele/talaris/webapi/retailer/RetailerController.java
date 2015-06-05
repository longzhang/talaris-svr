package me.ele.talaris.webapi.retailer;

import me.ele.talaris.model.Context;
import me.ele.talaris.model.Restaurant;
import me.ele.talaris.model.Station;
import me.ele.talaris.service.station.IStationService;
import me.ele.talaris.service.station.dto.RetailerWithOrderInfo;
import me.ele.talaris.web.framework.ResponseEntity;
import me.ele.talaris.web.framework.WebAPIBaseController;
import me.ele.talaris.web.log.report.InterfaceMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.ws.rs.QueryParam;
import java.util.List;

/**
 * Created by Daniel on 15/5/13.
 */
@Controller
@RequestMapping("/webapi/")
public class RetailerController extends WebAPIBaseController {
    Logger logger = LoggerFactory.getLogger(RetailerController.class);
    @Autowired
    IStationService stationService;

    /**
     * 创建站点
     *
     * @param mobile
     * @return
     */
    @RequestMapping(value = "/retailer/order_count")
    @InterfaceMonitor(interfaceName = "查询商家")
    public @ResponseBody
    ResponseEntity<List<RetailerWithOrderInfo>> queryRetailer(Context context,
                                                              @QueryParam(value = "status") Integer status) {
        ResponseEntity<List<RetailerWithOrderInfo>> responseEntity = null;
        Integer delivererId = context.getUser().getId();

        List<RetailerWithOrderInfo> retailerList = stationService.getRetailerWithOrderInfoListByDelivererId(context, delivererId, status);



        logger.info("查询商家，配送员ID:" + delivererId);
//        Station station = stationService.createStationByUser(user);
        if (retailerList != null) {
            responseEntity = new ResponseEntity<List<RetailerWithOrderInfo>>("200", "", retailerList);
        } else {
            responseEntity = new ResponseEntity<List<RetailerWithOrderInfo>>("GET_RESTUARANT_ERROR_100", "查询餐厅列表失败，请重试", null);
        }
        return responseEntity;
    }
}
