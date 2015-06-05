package me.ele.talaris.webapi.inter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ele.talaris.deliveryorder.dto.DeliveryOrder;
import me.ele.talaris.exception.SystemException;
import me.ele.talaris.exception.UserException;
import me.ele.talaris.hermes.service.IHermesService;
import me.ele.talaris.model.Context;

import me.ele.talaris.napos.service.INaposService;
import me.ele.talaris.service.deliveryorder.IGetDeliveryOrderService;
import me.ele.talaris.utils.SerializeUtil;
import me.ele.talaris.web.framework.ResponseEntity;
import me.ele.talaris.web.framework.WebAPIBaseController;
import me.ele.talaris.web.log.report.InterfaceMonitor;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/inter/")
public class InterController extends WebAPIBaseController {

    private final static Logger logger = LoggerFactory.getLogger(InterController.class);

    @Resource(name = "coffeeHermesService")
    private IHermesService coffeeHermesService;
    @Resource(name = "talarisNaposService")
    private INaposService talarisNaposService;
    @Value("${msg.voice.notify.template}")
    private String msgTemplate;
    @Autowired
    private IGetDeliveryOrderService deliveryOrderService;

    @RequestMapping(value = "/hermes/push")
    @InterfaceMonitor(interfaceName = "语音电话回调")
    public void postHermesTaskStatus(HttpServletRequest request, HttpServletResponse response,
            @RequestHeader("X-HERMES-AUTHENTICATE") String key) throws UserException, SystemException, IOException {

        if (!key.equals(coffeeHermesService.getSmsSenderKey())) {
            response.setStatus(400);
            return;
        }
        try {
            InputStream tInput = request.getInputStream();
            String jResult = IOUtils.toString(tInput);
            logger.info("Hermes post call task status:{}", jResult);
            HermesStatus result = SerializeUtil.jsonToBean(jResult, HermesStatus.class);
            long taskId = result.getData().getTask_id();
            String errorLog = result.getData().getError_log();
            int newStatus = result.getData().getNew_status();
            int updateTime = (int) result.getData().getUpdate_time();
            coffeeHermesService.updateCallTaskInfo(taskId, errorLog, newStatus, updateTime);
        } catch (Exception e) {
            logger.error("hermes push failed", e);
        }
    }

    @RequestMapping(value = "/walle/push")
    public void postWalleRestaurant(HttpServletRequest request, HttpServletResponse response,
            @RequestParam("manager_mobile") String managerMobile, @RequestParam("status") String status,
            @RequestParam("ele_id") String eleId) throws Exception {
        if (!isMobile(managerMobile)) {
            throw new SystemException("MOBILE_NOT_VALID", "手机号不合法");
        }
        try {
            int ele_id = Integer.parseInt(eleId);
            int st = Integer.parseInt(status);
        } catch (Exception e) {
            throw new SystemException("PARAM_NOT_VALID", "invalid parameter");
        }
        logger.debug("Walle post restaurant mobile:{}, status:{}, restaurantId:{}", managerMobile, status, eleId);
        if (Integer.parseInt(status) == 1) {
            talarisNaposService.recordRestaurantBindInfo(Integer.parseInt(eleId), managerMobile);
        } else {
            talarisNaposService.deactivateRestaurant(Integer.parseInt(eleId), managerMobile);
        }
    }

    @RequestMapping(value = "/walle/restaurant_bind/get")
    public @ResponseBody ResponseEntity<String> getWalleRestaurantBindInfo(HttpServletRequest request,
            HttpServletResponse response, @RequestParam("ele_id") String eleId) throws Exception {
        int ele_id;
        try {
            ele_id = Integer.parseInt(eleId);
        } catch (Exception e) {
            throw new SystemException("PARAM_NOT_VALID", "invalid parameter");
        }
        logger.debug("Walle get restaurant bind info restaurantId:{}", eleId);
        String mobile = talarisNaposService.getRestaurantBindInfo(ele_id);
        return ResponseEntity.success(mobile);
    }

    @RequestMapping(value = "/ping")
    public void talarisServerStatus(HttpServletRequest request, HttpServletResponse response) {
        response.setStatus(200);
        return;
    }

    private boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        // /^1[3|4|5|7|8]\d{9}$/
        // ^[1]([3][0-9]{1}|59|58|88|89|76)[0-9]{8}$
        p = Pattern.compile("^1[3|4|5|7|8]\\d{9}$"); // 验证手机号,
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    @RequestMapping(value = "/notification/order_arrived/audio", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Map<Long, Long>> callCustomer(Context context,
            @RequestParam("order_id") long orderId) throws UserException {
        ResponseEntity<Map<Long, Long>> responseEntity = null;
        Map<Long, Long> rs = new HashMap<>();
        List<Long> orderIds = new ArrayList<>();
        orderIds.add(orderId);
        List<DeliveryOrder> orders = deliveryOrderService.getDeliverOrdersByDeliveryIdListWithOutAnyValid(context, orderIds);

        if (orders != null && orders.size() > 0) {
            String customerMobile = String.valueOf(orders.get(0).getReceiver_mobile());
            Long taskId = coffeeHermesService.sendAudioMessage(customerMobile, msgTemplate, orderId);
            rs.put(orderId, taskId);
            logger.info("自动拨打电话给[" + customerMobile + "]");
        }else{
            logger.debug("no any order found with order id:" + orderId);
        }
        responseEntity = new ResponseEntity<Map<Long, Long>>("200", "", rs);

        return responseEntity;
    }
}
