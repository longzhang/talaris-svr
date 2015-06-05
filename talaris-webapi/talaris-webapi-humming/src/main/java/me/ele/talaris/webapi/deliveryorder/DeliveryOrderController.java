package me.ele.talaris.webapi.deliveryorder;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import me.ele.talaris.constant.Constant;
import me.ele.talaris.constant.DeliveryOrderContant;
import me.ele.talaris.deliveryorder.dto.DeliveryOrder;
import me.ele.talaris.deliveryorder.dto.DeliveryOrderEx;
import me.ele.talaris.deliveryorder.dto.GetDeliveryOrderFilter;
import me.ele.talaris.deliveryorder.dto.PartDeliveryOrder;
import me.ele.talaris.deliveryorder.persistent.eb.DeliveryOrderRecord;
import me.ele.talaris.eleorderid.generate.IdGenerator;
import me.ele.talaris.exception.UserException;
import me.ele.talaris.model.CallTaskInfoEx;
import me.ele.talaris.model.Context;
import me.ele.talaris.model.Restaurant;
import me.ele.talaris.model.Station;
import me.ele.talaris.service.deliveryorder.ICancelDeliveryOrderService;
import me.ele.talaris.service.deliveryorder.IConfirmDeliverdService;
import me.ele.talaris.service.deliveryorder.ICreateDeliveryOrderService;
import me.ele.talaris.service.deliveryorder.IGetCallTaskInfoService;
import me.ele.talaris.service.deliveryorder.IGetDeliveryOrderService;
import me.ele.talaris.service.deliveryorder.IGetWaitToDeliveryOrderService;
import me.ele.talaris.service.deliveryorder.IMarkExceptionService;
import me.ele.talaris.service.deliveryorder.IMarkNormalService;
import me.ele.talaris.service.deliveryorder.INotifyUserService;
import me.ele.talaris.service.deliveryorder.impl.CommonDeliveryOrderService;
import me.ele.talaris.service.deliveryorder.impl.DeliveryOrderComponent;
import me.ele.talaris.service.permission.IPermissionService;
import me.ele.talaris.utils.Utils;
import me.ele.talaris.web.framework.ResponseEntity;
import me.ele.talaris.web.framework.WebAPIBaseController;
import me.ele.talaris.web.framework.compress.Compress;
import me.ele.talaris.web.log.report.InterfaceMonitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/webapi/")
public class DeliveryOrderController extends WebAPIBaseController {
    public static final Logger LOGGER = LoggerFactory.getLogger(DeliveryOrderController.class);
    @Autowired
    IPermissionService permitionValidateService;
    @Autowired
    ICreateDeliveryOrderService createDeliveryOrderService;
    @Autowired
    IGetDeliveryOrderService getDeliveryOrderService;
    @Autowired
    INotifyUserService notifyUserService;
    @Autowired
    IConfirmDeliverdService ConfirmDeliveredService;
    @Autowired
    IMarkExceptionService markExceptionService;
    @Autowired
    ICancelDeliveryOrderService cancelDeliveryOrderService;
    @Autowired
    IGetCallTaskInfoService getCallTaskInfoService;
    @Autowired
    IGetWaitToDeliveryOrderService getWaitToDeliveryOrderService;
    @Autowired
    IMarkNormalService markNormalService;
    @Autowired
    DeliveryOrderComponent deliveryOrderComponent;
    @Autowired
    IdGenerator idGenerator;
    @Autowired
    CommonDeliveryOrderService commonDeliveryOrderService;

    /**
     * 创建配送单
     * 
     * @param context
     * @param eleOrderIds
     * @param createType
     * @return
     * @throws UserException
     */
    @RequestMapping(value = "delivery_order/create", method = RequestMethod.POST)
    @InterfaceMonitor(interfaceName = "创建配送单", contextIndex = 0)
    public @Compress @ResponseBody ResponseEntity<List<DeliveryOrderEx>> create(Context context, @RequestParam(
            value = "ele_order_id_list", required = false) String[] eleOrderIds,
            @RequestParam("create_type") String createType,
            @RequestParam(value = "customer_phone", required = false) String customerPhone) throws UserException {
        ResponseEntity<List<DeliveryOrderEx>> responseEntity = null;

        if (!permitionValidateService.hasRole(context, Constant.COURIER)) {
            responseEntity = new ResponseEntity<List<DeliveryOrderEx>>("DELIVERY_ORDER_ERROR_561", "创建配送单失败", null);
            return responseEntity;
        }
        List<DeliveryOrder> deliveryOrders = new ArrayList<>();
        if (DeliveryOrderContant.CREATE_ORDER_MANUALLY.equals(createType)) {
            if(!Utils.isMobile(customerPhone)){
                responseEntity = new ResponseEntity<List<DeliveryOrderEx>>("DELIVERY_ORDER_ERROR_564", "输入手机号非法<br>请重新输入",
                        null);
                return responseEntity;
            }
            if (customerPhone == null) {
                responseEntity = new ResponseEntity<List<DeliveryOrderEx>>("DELIVERY_ORDER_ERROR_563", "收货人联系电话不能为空",
                        null);
                return responseEntity;
            }

            DeliveryOrder tmpOrder = new DeliveryOrder();
            tmpOrder.setReceiver_name("");
            tmpOrder.setReceiver_address("");
            tmpOrder.setRst_id(Integer.valueOf(commonDeliveryOrderService.getDefaultRstIds()));
            tmpOrder.setStatus(DeliveryOrderContant.DELIVERYING);
            tmpOrder.setCreated_type(Integer.valueOf(createType));
            tmpOrder.setTotal_amount(new BigDecimal(0));
            tmpOrder.setPaied_amount(new BigDecimal(0));
            tmpOrder.setCreated_at(new Timestamp(System.currentTimeMillis()));
            tmpOrder.setUpdated_at(new Timestamp(System.currentTimeMillis()));
            tmpOrder.setReceiver_mobile(Long.valueOf(customerPhone));
            tmpOrder.setTaker_id(context.getUser().getId());
            tmpOrder.setUuid(UUID.randomUUID().toString());
            tmpOrder.setTaker_mobile(context.getUser().getMobile());
            tmpOrder.setSource(DeliveryOrderContant.SOURCE_FROM_MANAULLY);
            tmpOrder.setEle_order_id(idGenerator.getEleOrderId());
            tmpOrder.setEle_order_sn(idGenerator.getEleOrderSn());
            DeliveryOrder deliveryOrder = createDeliveryOrderService.createDeliveryOrder(context, tmpOrder, tmpOrder.getReceiver_mobile());
            deliveryOrders.add(deliveryOrder);
        } else {
            if (eleOrderIds == null) {
                responseEntity = new ResponseEntity<List<DeliveryOrderEx>>("DELIVERY_ORDER_ERROR_562", "ELE订单id不能为空",
                        null);
                return responseEntity;
            }
            deliveryOrders = createDeliveryOrderService.fetchDeliveryOrders(context, eleOrderIds, createType);
        }

        List<DeliveryOrderEx> deliveryOrderExs = changeDeliveryOrdersToDeliveryOrderExs(deliveryOrders);
        responseEntity = new ResponseEntity<List<DeliveryOrderEx>>("200", "", deliveryOrderExs);
        return responseEntity;
    }

    /**
     * 配送单标记异常
     * 
     * @param context
     * @param deliveryOrderId
     * @param remark
     * @return
     * @throws UserException
     */
    @RequestMapping(value = "delivery_order/mark_exception", method = RequestMethod.POST)
    @InterfaceMonitor(interfaceName = "标记异常", contextIndex = 0)
    public @ResponseBody ResponseEntity<DeliveryOrderEx> markException(Context context,
            @RequestParam("id") String deliveryOrderId, @RequestParam("remark") String remark) throws UserException {
        ResponseEntity<DeliveryOrderEx> responseEntity = null;
        if (!permitionValidateService.hasRole(context, Constant.COURIER)) {
            responseEntity = new ResponseEntity<DeliveryOrderEx>("DELIVERY_ORDER_ERROR_590", "系统异常", null);
            return responseEntity;
        }
        if (StringUtils.isEmpty(remark)) {
            remark = "未填写";
        }
        // 对remark进行转义
        remark = Utils.isValidate(remark);
        LOGGER.info("校验后的字符串为：{}", remark);
        if (StringUtils.isEmpty(remark.trim())) {
            remark = "未填写";
        }
        DeliveryOrder result = markExceptionService.markException(context, deliveryOrderId, remark);
        if (result != null) {
            DeliveryOrderEx deliveryOrderEx = new DeliveryOrderEx(result);
            responseEntity = new ResponseEntity<DeliveryOrderEx>("200", "", deliveryOrderEx);
            return responseEntity;
        }
        return new ResponseEntity<DeliveryOrderEx>("200", "", new DeliveryOrderEx());
    }

    /**
     * 标记正常
     * 
     * @param context
     * @param deliveryOrderIds
     * @return
     * @throws UserException
     */
    @RequestMapping(value = "delivery_order/mark_normal", method = RequestMethod.POST)
    @InterfaceMonitor(interfaceName = "标记正常", contextIndex = 0)
    public @ResponseBody ResponseEntity<Map<String, DeliveryOrderEx>> markNormal(Context context,
            @RequestParam("id_list") String[] deliveryOrderIds) throws UserException {
        ResponseEntity<Map<String, DeliveryOrderEx>> responseEntity = null;

        if (!permitionValidateService.hasRole(context, Constant.COURIER)) {
            responseEntity = new ResponseEntity<Map<String, DeliveryOrderEx>>("DELIVERY_ORDER_ERROR_600", "系统异常", null);
            return responseEntity;
        }
        Map<Long, DeliveryOrder> result = markNormalService.markNormal(context, deliveryOrderIds);
        if (CollectionUtils.isEmpty(result)) {
            responseEntity = new ResponseEntity<Map<String, DeliveryOrderEx>>("DELIVERY_ORDER_ERROR_600", "系统异常", null);
            return responseEntity;
        }
        Collection<DeliveryOrder> values = result.values();
        // 判断是否全部是0，如果是0则更新失败
        boolean isAllFail = true;
        for (DeliveryOrder value : values) {
            if (value != null) {
                isAllFail = false;
                break;
            }
        }
        Map<String, DeliveryOrderEx> realResult = new HashMap<String, DeliveryOrderEx>();
        realResult = changeDeliverOrderMapToDeliverOrderExMap(result);
        if (!isAllFail) {
            responseEntity = new ResponseEntity<Map<String, DeliveryOrderEx>>("200", "", realResult);
            return responseEntity;
        } else {
            responseEntity = new ResponseEntity<Map<String, DeliveryOrderEx>>("DELIVERY_ORDER_ERROR_600", "系统异常，请重试",
                    realResult);
            return responseEntity;
        }
    }

    /**
     * 通知用户
     * 
     * @param context
     * @param deliveryOrderIds
     * @return
     * @throws UserException
     */
    @RequestMapping(value = "delivery_order/notify_customer", method = RequestMethod.POST)
    @InterfaceMonitor(interfaceName = "电话通知用户", contextIndex = 0)
    public @ResponseBody ResponseEntity<Map<String, String>> notifyCustomer(Context context,
            @RequestParam("id_list") String[] deliveryOrderIds) throws UserException {
        ResponseEntity<Map<String, String>> responseEntity = null;

        if (!permitionValidateService.hasRole(context, Constant.COURIER)) {
            responseEntity = new ResponseEntity<Map<String, String>>("DELIVERY_ORDER_ERROR_580", "系统异常", null);
            return responseEntity;
        }
        Map<Long, Long> result = notifyUserService.notifyCustomer(context, deliveryOrderIds);
        if (CollectionUtils.isEmpty(result)) {
            responseEntity = new ResponseEntity<Map<String, String>>("DELIVERY_ORDER_ERROR_580", "系统异常", null);
            return responseEntity;
        }
        Collection<Long> values = result.values();
        // 判断是否全部是0，如果是0则更新失败
        boolean isAllFail = true;
        Iterator<Long> iterator = values.iterator();
        while (iterator.hasNext()) {
            Long value = iterator.next();
            if (value != 0) {
                isAllFail = false;
                break;
            }
        }
        Map<String, String> stringMap = new HashMap<String, String>();
        stringMap = changeLongMapToStringMap(result);
        if (!isAllFail) {
            responseEntity = new ResponseEntity<Map<String, String>>("200", "", stringMap);
            return responseEntity;
        } else {
            responseEntity = new ResponseEntity<Map<String, String>>("DELIVERY_ORDER_ERROR_580", "系统异常，请重试", null);
            return responseEntity;
        }
    }

    /**
     * 确认送达
     * 
     * @param context
     * @param deliveryOrderIds
     * @return
     * @throws UserException
     */
    @RequestMapping(value = "delivery_order/confirm_delivered", method = RequestMethod.POST)
    @InterfaceMonitor(interfaceName = "确认送达", contextIndex = 0)
    public @ResponseBody ResponseEntity<Map<String, Integer>> confirmDelivered(Context context,
            @RequestParam("id_list") String[] deliveryOrderIds) throws UserException {
        ResponseEntity<Map<String, Integer>> responseEntity = null;

        if (!permitionValidateService.hasRole(context, Constant.COURIER)) {
            responseEntity = new ResponseEntity<Map<String, Integer>>("DELIVERY_ORDER_ERROR_610", "系统异常", null);
            return responseEntity;
        }
        Map<Long, Integer> result = ConfirmDeliveredService.confirmDelivered(context, deliveryOrderIds);
        if (result == null) {
            responseEntity = new ResponseEntity<Map<String, Integer>>("DELIVERY_ORDER_ERROR_610", "系统异常", null);
            return responseEntity;
        }
        Collection<Integer> values = result.values();
        // 判断是否全部是0，如果是0则更新失败
        boolean isAllFail = true;
        for (Integer value : values) {
            if (value == 1) {
                isAllFail = false;
                break;
            }
        }
        Map<String, Integer> realResult = changeLongAsKeyMapToStringMap(result);
        if (!isAllFail) {
            responseEntity = new ResponseEntity<Map<String, Integer>>("200", "", realResult);
            return responseEntity;
        } else {
            responseEntity = new ResponseEntity<Map<String, Integer>>("DELIVERY_ORDER_ERROR_610", "系统异常，请重试",
                    realResult);
            return responseEntity;
        }
    }

    /**
     * 取消配送单
     * 
     * @param context
     * @param deliveryOrderId
     * @return
     * @throws UserException
     */
    @RequestMapping(value = "delivery_order/cancel", method = RequestMethod.POST)
    @InterfaceMonitor(interfaceName = "取消配送单", contextIndex = 0)
    public @ResponseBody ResponseEntity<Map<String, Integer>> cancelMyDeliveryOrder(Context context,
            @RequestParam("delivery_order_id") String deliveryOrderId) throws UserException {
        ResponseEntity<Map<String, Integer>> responseEntity = null;

        if (!permitionValidateService.hasRole(context, Constant.COURIER)) {
            responseEntity = new ResponseEntity<Map<String, Integer>>("APP_ERROR_553", "系统异常", null);
            return responseEntity;
        }
        Map<String, Integer> result = cancelDeliveryOrderService.cancel(context, deliveryOrderId);
        if (CollectionUtils.isEmpty(result)) {
            responseEntity = new ResponseEntity<Map<String, Integer>>("APP_ERROR_553", "系统异常", null);
            return responseEntity;
        }
        if (!CollectionUtils.isEmpty(result)) {
            responseEntity = new ResponseEntity<Map<String, Integer>>("200", null, result);
            return responseEntity;
        } else {
            responseEntity = new ResponseEntity<Map<String, Integer>>("APP_ERROR_553", "取消配送单配送失败", null);
            return responseEntity;
        }
    }

    /**
     * 配送单查询
     * 
     * @param context
     * @param deliveryOrderFilter
     * @return
     * @throws UserException
     */
    @RequestMapping(value = "delivery_order/", method = RequestMethod.GET)
    @InterfaceMonitor(interfaceName = "配送单查询", contextIndex = 0)
    public @ResponseBody ResponseEntity<List<DeliveryOrderEx>> getDeliveryOrders(Context context,
            @ModelAttribute GetDeliveryOrderFilter deliveryOrderFilter, @RequestHeader(value = "HTTP-DEVICE-TYPE",
                    required = false, defaultValue = "4") String deviceType) throws UserException {
        ResponseEntity<List<DeliveryOrderEx>> responseEntity = null;
        if (!permitionValidateService.hasRole(context, Constant.COURIER + "," + Constant.STATIONMANAGER)) {
            responseEntity = new ResponseEntity<List<DeliveryOrderEx>>("DELIVERY_ORDER_ERROR_561", "获取配送单失败", null);
            return responseEntity;
        }
        List<DeliveryOrder> deliveryOrders = getDeliveryOrderService.getDeliveryOrders(deliveryOrderFilter, context,
                deviceType);
        List<DeliveryOrderEx> deliveryOrderExs = changeDeliveryOrdersToDeliveryOrderExs(deliveryOrders);
        responseEntity = new ResponseEntity<List<DeliveryOrderEx>>("200", "",
                CollectionUtils.isEmpty(deliveryOrderExs) ? new ArrayList<DeliveryOrderEx>() : deliveryOrderExs);
        return responseEntity;
    }

    /**
     * 配送单查询
     *
     * @param context
     * @param deliveryOrderFilter
     * @return
     * @throws UserException
     */
    @RequestMapping(value = "delivery_order/node", method = RequestMethod.GET)
    @InterfaceMonitor(interfaceName = "配送单查询", contextIndex = 0)
    public @ResponseBody ResponseEntity<List<DeliveryOrder>> getDeliveryOrdersAndSetDelivering(Context context,
            @RequestParam("rst_id") String rst_ids) throws UserException {
        ResponseEntity<List<DeliveryOrder>> responseEntity = null;

        if (!permitionValidateService.hasRole(context, Constant.COURIER)) {
            responseEntity = new ResponseEntity<List<DeliveryOrder>>("DELIVERY_ORDER_ERROR_561", "您没有权限", null);
            return responseEntity;
        }
        List<DeliveryOrder> deliveryOrders = new ArrayList<>();
        if (rst_ids == null || rst_ids.length() == 0) {
            responseEntity = new ResponseEntity<List<DeliveryOrder>>("RESTAURANT_ERROR_001", "商店id不能为空", null);
            return responseEntity;
        }
        String[] ids = rst_ids.split(",");

        if (ids == null || ids.length == 0) {
            responseEntity = new ResponseEntity<List<DeliveryOrder>>("RESTAURANT_ERROR_001", "商店id不能为空", null);
            return responseEntity;
        }

        for (String id : ids) {
            List<DeliveryOrder> tmp = getWaitToDeliveryOrderService.getDeliveryOrdersByContext(context,
                    Integer.valueOf(id));
            deliveryOrders.addAll(tmp);
        }

        deliveryOrderComponent.addRstName(deliveryOrders);

        if(deliveryOrders != null){
            for(DeliveryOrder order: deliveryOrders){
                Restaurant rst = deliveryOrderComponent.getRestaurantByEleRstId(order.getRst_id());
                if (rst == null) {
                    responseEntity = new ResponseEntity<List<DeliveryOrder>>("WALLE_POST_ERROR_002", "餐厅不存在", null);
                    return responseEntity;
                }
                Station station = createDeliveryOrderService.getStationByRestaurantId(rst.getId());
                if (station == null) {
                    responseEntity = new ResponseEntity<List<DeliveryOrder>>("STATION_ERROR_008", "站点不存在", null);
                    return responseEntity;
                }
                createDeliveryOrderService.updateDeliveryOrderStatus(station.getId(), DeliveryOrderContant.DELIVERYING,
                        order.getId(), context.getUser().getId(), context.getUser().getMobile());
                deliveryOrderComponent.setEleOrderDetail(order);
                DeliveryOrderRecord orderRecord = new DeliveryOrderRecord(order, context.getUser().getId(),
                        DeliveryOrderContant.OPERATION_DELIVERYING, "");
                deliveryOrderComponent.insertDeliveryOrderRecord(orderRecord);
            }
        }

        responseEntity = new ResponseEntity<List<DeliveryOrder>>("200", "",
                CollectionUtils.isEmpty(deliveryOrders) ? new ArrayList<DeliveryOrder>() : deliveryOrders);

        return responseEntity;
    }

    /**
     * 配送员查看自己所在站点对应的配送单，这里的配送单拉取出来后全部是待配送的
     * 
     * @param context
     * @param deliveryOrderFilter
     * @return
     * @throws UserException
     */
    @RequestMapping(value = "my/station/wait_to_delivery_order")
    @InterfaceMonitor(interfaceName = "获取待配送的配送单", contextIndex = 0)
    public @Compress @ResponseBody ResponseEntity<List<PartDeliveryOrder>> getDeliveryOrders(Context context,
            @RequestParam("rst_id") Integer rst_id) throws UserException {
        ResponseEntity<List<PartDeliveryOrder>> responseEntity = null;

        if (!permitionValidateService.hasRole(context, Constant.COURIER)) {
            responseEntity = new ResponseEntity<List<PartDeliveryOrder>>("DELIVERY_ORDER_ERROR_561", "您没有权限", null);
            return responseEntity;
        }

        List<DeliveryOrder> deliveryOrders = getWaitToDeliveryOrderService.getDeliveryOrdersByContext(context, rst_id);
        List<DeliveryOrderEx> deliveryOrderExs = changeDeliveryOrdersToDeliveryOrderExs(deliveryOrders);
        sort(deliveryOrderExs);
        List<PartDeliveryOrder> partDeliveryOrders = getWaitToDeliveryOrderService
                .changeLongDeliveryOrderToSort(deliveryOrderExs);
        responseEntity = new ResponseEntity<List<PartDeliveryOrder>>("200", "",
                CollectionUtils.isEmpty(partDeliveryOrders) ? new ArrayList<PartDeliveryOrder>() : partDeliveryOrders);
        return responseEntity;
    }

    /**
     * 语音电话状态查询接口
     *
     * @param context
     * @return
     * @throws UserException
     */
    @RequestMapping(value = "delivery_order/call_receiver_status")
    @InterfaceMonitor(interfaceName = "语音电话查询", contextIndex = 0)
    public @ResponseBody ResponseEntity<List<CallTaskInfoEx>> getDeliveryOrders(Context context,
            @RequestParam("task_id_list") List<Long> taskIdList) throws UserException {
        ResponseEntity<List<CallTaskInfoEx>> responseEntity = null;

        if (!permitionValidateService.hasRole(context, Constant.COURIER)) {
            responseEntity = new ResponseEntity<List<CallTaskInfoEx>>("DELIVERY_ORDER_ERROR_561", "您没有权限", null);
            return responseEntity;
        }
        List<CallTaskInfoEx> callTaskInfoExs = getCallTaskInfoService.getCallTaskInfosByIdList(taskIdList);
        responseEntity = new ResponseEntity<List<CallTaskInfoEx>>("200", "",
                CollectionUtils.isEmpty(callTaskInfoExs) ? new ArrayList<CallTaskInfoEx>() : callTaskInfoExs);
        return responseEntity;
    }

    private List<DeliveryOrderEx> changeDeliveryOrdersToDeliveryOrderExs(List<DeliveryOrder> deliveryOrders) {
        List<DeliveryOrderEx> deliveryOrderExs = new ArrayList<DeliveryOrderEx>();
        if (CollectionUtils.isEmpty(deliveryOrders)) {
            return null;
        }
        for (DeliveryOrder deliveryOrder : deliveryOrders) {
            deliveryOrderExs.add(new DeliveryOrderEx(deliveryOrder));
        }
        return deliveryOrderExs;
    }

    /**
     * 自动更新订单状态到确认送达
     *
     * @param context
     * @return
     * @throws UserException
     */
    @RequestMapping(value = "delivery_order/auto_confirmed", method = RequestMethod.POST)
    @InterfaceMonitor(interfaceName = "自动更新订单状态到已送达", contextIndex = 0)
    public @ResponseBody ResponseEntity<String> autoConfirmed(Context context) throws UserException {
        getDeliveryOrderService.autoUpdateDeliveryingOrderStatus();

        return ResponseEntity.success("success");
    }

    private Map<String, DeliveryOrderEx> changeDeliverOrderMapToDeliverOrderExMap(Map<Long, DeliveryOrder> result) {
        if (CollectionUtils.isEmpty(result)) {
            return null;
        }
        Map<String, DeliveryOrderEx> deliverOrderExMap = new HashMap<String, DeliveryOrderEx>();
        Iterator<Entry<Long, DeliveryOrder>> iterator = result.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<Long, DeliveryOrder> entry = iterator.next();
            if (entry.getValue() != null) {
                deliverOrderExMap.put(String.valueOf(entry.getKey()), new DeliveryOrderEx(entry.getValue()));
            } else {
                deliverOrderExMap.put(String.valueOf(entry.getKey()), null);
            }
        }
        return deliverOrderExMap;
    }

    private Map<String, String> changeLongMapToStringMap(Map<Long, Long> longMap) {
        if (CollectionUtils.isEmpty(longMap)) {
            return null;
        }
        Map<String, String> stringMap = new HashMap<String, String>();
        Iterator<Entry<Long, Long>> iterator = longMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<Long, Long> entry = iterator.next();
            stringMap.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
        }
        return stringMap;
    }

    private Map<String, Integer> changeLongAsKeyMapToStringMap(Map<Long, Integer> longMap) {
        if (CollectionUtils.isEmpty(longMap)) {
            return null;
        }
        Map<String, Integer> stringMap = new HashMap<String, Integer>();
        Iterator<Entry<Long, Integer>> iterator = longMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<Long, Integer> entry = iterator.next();
            stringMap.put(String.valueOf(entry.getKey()), entry.getValue());
        }
        return stringMap;
    }

    private static final Comparator<DeliveryOrderEx> COMPARATOR = new Comparator<DeliveryOrderEx>() {
        public int compare(DeliveryOrderEx o1, DeliveryOrderEx o2) {
            return new Integer(o2.getEle_order_sn()).compareTo(new Integer(o1.getEle_order_sn()));
        }
    };

    private void sort(List<DeliveryOrderEx> deliveryOrderExs) {
        if (CollectionUtils.isEmpty(deliveryOrderExs)) {
            return;
        }
        Collections.sort(deliveryOrderExs, COMPARATOR);
    }

}
