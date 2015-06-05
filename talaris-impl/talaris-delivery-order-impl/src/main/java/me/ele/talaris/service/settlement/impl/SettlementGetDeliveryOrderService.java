package me.ele.talaris.service.settlement.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.ele.talaris.constant.Constant;
import me.ele.talaris.constant.DeliveryOrderContant;
import me.ele.talaris.context.utils.ContextUtil;
import me.ele.talaris.deliveryorder.dto.DeliveryOrder;
import me.ele.talaris.deliveryorder.persistent.dao.DeliveryOrderDao;
import me.ele.talaris.dao.RestaurantDao;
import me.ele.talaris.dao.UserStationRoleDao;
import me.ele.talaris.exception.UserException;
import me.ele.talaris.model.Context;
import me.ele.talaris.model.Restaurant;
import me.ele.talaris.model.settlement.DeliveryOrderPartInfo;
import me.ele.talaris.model.settlement.TheLastSettleInfo;
import me.ele.talaris.model.settlement.ViewDeliveryOrderInfos;
import me.ele.talaris.service.deliveryorder.impl.DeliveryOrderComponent;
import me.ele.talaris.service.permission.IPermissionService;
import me.ele.talaris.service.settlement.ISettlementGetDeliveryOrderService;
import me.ele.talaris.utils.Pair;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * 结算模块查看配送单
 * 
 * @author zhengwen
 *
 */
@Service
public class SettlementGetDeliveryOrderService implements ISettlementGetDeliveryOrderService {
    public static final Logger logger = LoggerFactory.getLogger(SettlementGetDeliveryOrderService.class);
    @Autowired
    DeliveryOrderComponent deliveryOrderComponent;
    @Autowired
    DeliveryOrderDao deliveryOrderDao;
    @Autowired
    IPermissionService permitionValidateService;
    @Autowired
    SettlementComponent settlementComponent;
    @Autowired
    UserStationRoleDao userStationRoleDao;
    @Autowired
    RestaurantDao restaurantDao;

    @Override
    public ViewDeliveryOrderInfos getDeliveryOrder(Context context, int takerId, int status, Integer paymentType,
            Integer pageNow, Integer pageSize, int rst_id, String detailLevel) throws UserException {
        // 需要判断配送员是否在该餐厅下，或者是否为自己

        int valiateResult = validateCanWatchDeliveryOrders(context, takerId);
        logger.debug("谁在查看：{}", valiateResult);
        if (valiateResult == -1) {
            return null;
        }
        ViewDeliveryOrderInfos viewDeliveryOrderInfos = new ViewDeliveryOrderInfos();
        // 前端接口查看某种状态的配送单,需要知道配送单总量和配送单的总金额
        TheLastSettleInfo theLastSettleInfo = settlementComponent.getTheLastSettleTimeByTakerId(takerId, context,
                rst_id);
        Timestamp start = theLastSettleInfo.getSettleTime();
        Pair<List<DeliveryOrder>, Long> deliveryOrders;
        // Timestamp lastSettleTime = settlementComponent.getTheLastSettleTimeByTakerId(takerId,
        // context).getSettleTime();
        // settlementComponent.systemMarkException(context, takerId, lastSettleTime);
        if (paymentType == null) {
            logger.debug("case3");
            deliveryOrders = deliveryOrderDao.selectPageOneTakerOneStatusDeliveryOrder(rst_id, pageNow, pageSize,
                    takerId, status, start);
        } else {
            logger.debug("paymentType {} start= {}", paymentType, start);
            deliveryOrders = deliveryOrderDao.selectPageOneTakerOneStatusOnePaymentDeliveryOrder(rst_id, pageNow,
                    pageSize, takerId, status, paymentType, start);
        }
        List<DeliveryOrder> orders = deliveryOrders.first;
        // 校验在不在自己站点下面
        if (valiateResult == 1) {
            int stationID = ContextUtil.getMyStationIdByContext(context);
            Iterator<DeliveryOrder> iterator = orders.iterator();
            while (iterator.hasNext()) {
                DeliveryOrder deliveryOrder = iterator.next();
                if (stationID != deliveryOrder.getStation_id()) {
                    iterator.remove();
                }
            }
        }
        // 自己查看配送单，因为一对多的需求出现了，所以前端会传递一个rst_id过来。
        else {
            Iterator<DeliveryOrder> iterator = orders.iterator();
            while (iterator.hasNext()) {
                DeliveryOrder deliveryOrder = iterator.next();
                if (rst_id != deliveryOrder.getRst_id()) {
                    iterator.remove();
                }
            }
        }
        // 为什么要先查询一次饿单详情，因为这里不管detail_level是多少，都需要饿单的创建时间，所以先查询
        deliveryOrderComponent.addEleOrderDetailJson(deliveryOrders.first);
        deliveryOrderComponent.addReMarks(deliveryOrders.first);
        List<DeliveryOrderPartInfo> deliveryOrderPartInfos = changeDeliveryOrdersToDeliveryOrderPartInfos(deliveryOrders.first);
        addEleOrderCreateTime(deliveryOrderPartInfos);
        if (!DeliveryOrderContant.DETAIL_LEVER_ONE.equals(detailLevel)) {
            removeEleOrderDetailJson(deliveryOrderPartInfos);
        }
        viewDeliveryOrderInfos.setDeliveryOrderList(deliveryOrderPartInfos);
        viewDeliveryOrderInfos.setTotal_count(deliveryOrders.second);
        Long onLineCount = deliveryOrderDao.getDifferentPaymentOrdersCount(takerId, rst_id,
                DeliveryOrderContant.ONLINE_PAID, status, start);
        Long offLineCount = deliveryOrderDao.getDifferentPaymentOrdersCount(takerId, rst_id,
                DeliveryOrderContant.CASH_ON_DELIVERY, status, start);
        viewDeliveryOrderInfos.setTotal_online_count(onLineCount);
        viewDeliveryOrderInfos.setTotal_offline_count(offLineCount);
        return viewDeliveryOrderInfos;
    }

    /**
     * 在每个DeliveryOrderPartInfo中添加配送单的创建时间
     * 
     * @param deliveryOrderPartInfos
     */
    private void addEleOrderCreateTime(List<DeliveryOrderPartInfo> deliveryOrderPartInfos) {
        if (CollectionUtils.isEmpty(deliveryOrderPartInfos)) {
            return;
        }
        for (DeliveryOrderPartInfo deliveryOrderPartInfo : deliveryOrderPartInfos) {
            String eleOrderDetailJson = deliveryOrderPartInfo.getEle_order_detail();
            Long createTime = deliveryOrderComponent.getTime(eleOrderDetailJson, Constant.two);
            deliveryOrderPartInfo.setCreate_order_time(new Timestamp(createTime));
        }
    }

    /**
     * 删除饿单详情
     * 
     * @param deliveryOrderPartInfos
     */
    private void removeEleOrderDetailJson(List<DeliveryOrderPartInfo> deliveryOrderPartInfos) {
        if (CollectionUtils.isEmpty(deliveryOrderPartInfos)) {
            return;
        }
        for (DeliveryOrderPartInfo deliveryOrderPartInfo : deliveryOrderPartInfos) {
            deliveryOrderPartInfo.setEle_order_detail("");
        }
    }

    /**
     * 校验是否能查看相应的配送单信息,返回-1是没权限查看，0是自己查看，1是管理员查看
     * 
     * @param context
     * @param takerId
     * @return
     * @throws UserException
     */
    private int validateCanWatchDeliveryOrders(Context context, int takerId) throws UserException {
        int userId = context.getUser().getId();
        if (userId == takerId) {
            return 0;
        }
        if (!permitionValidateService.hasRole(context, Constant.STATIONMANAGER)) {
            return -1;
        }
        List<Integer> userIDs = userStationRoleDao.listUserIdByStationId(ContextUtil.getMyStationIdByContext(context));
        if (CollectionUtils.isEmpty(userIDs)) {
            return -1;
        }
        for (Integer userID : userIDs) {
            if (userID == takerId) {
                return 1;
            }
        }
        return -1;
    }

    /**
     * 将数据库表里配送单转化为对应的前端展示的部分配送单信息
     * `
     * 
     * @param deliveryOrders
     * @return
     */
    private List<DeliveryOrderPartInfo>
            changeDeliveryOrdersToDeliveryOrderPartInfos(List<DeliveryOrder> deliveryOrders) {
        if (CollectionUtils.isEmpty(deliveryOrders)) {
            return null;
        }
        List<DeliveryOrderPartInfo> deliveryOrderPartInfos = new ArrayList<DeliveryOrderPartInfo>();
        for (DeliveryOrder deliveryOrder : deliveryOrders) {
            int rst_id = deliveryOrder.getRst_id();
            Restaurant restaurant = restaurantDao.getRestaurantByEleId(rst_id);
            DeliveryOrderPartInfo deliveryOrderPartInfo = new DeliveryOrderPartInfo(deliveryOrder, restaurant.getName());
            deliveryOrderPartInfos.add(deliveryOrderPartInfo);
        }
        return deliveryOrderPartInfos;

    }

    @Override
    public ViewDeliveryOrderInfos takerGetOwnDeliveryOrder(Context context, int status, int paymentType, int takerId,
            int rst_id, int detailLevel) throws UserException {
        ViewDeliveryOrderInfos viewDeliveryOrderInfos = new ViewDeliveryOrderInfos();
        TheLastSettleInfo theLastSettleInfo = settlementComponent.getTheLastSettleTimeByTakerId(takerId, context,
                rst_id);
        Timestamp start = theLastSettleInfo.getSettleTime();
        List<DeliveryOrder> deliveryOrders = deliveryOrderDao.listOneStatusOnePaymentTypeDeliveryOrders(status,
                takerId, paymentType, start);
        Iterator<DeliveryOrder> iterator = deliveryOrders.iterator();
        while (iterator.hasNext()) {
            DeliveryOrder deliveryOrder = iterator.next();
            if (rst_id != deliveryOrder.getRst_id()) {
                iterator.remove();
            }
        }

        // 为什么要先查询一次饿单详情，因为这里不管detail_level是多少，都需要饿单的创建时间，所以先查询
        deliveryOrderComponent.addEleOrderDetailJson(deliveryOrders);
        deliveryOrderComponent.addCallRecords(deliveryOrders);
        List<DeliveryOrderPartInfo> deliveryOrderPartInfos = changeDeliveryOrdersToDeliveryOrderPartInfos(deliveryOrders);
        addEleOrderCreateTime(deliveryOrderPartInfos);
        if (!DeliveryOrderContant.DETAIL_LEVER_ONE.equals(detailLevel)) {
            removeEleOrderDetailJson(deliveryOrderPartInfos);
        }
        viewDeliveryOrderInfos.setDeliveryOrderList(deliveryOrderPartInfos);
        viewDeliveryOrderInfos.setTotal_count(CollectionUtils.isEmpty(deliveryOrderPartInfos) ? 0
                : deliveryOrderPartInfos.size());
        // Long onLineCount = deliveryOrderDao.getDifferentPaymentOrdersCount(takerId, DeliveryOrderContant.ONLINE_PAID,
        // status, start);
        // Long offLineCount = deliveryOrderDao.getDifferentPaymentOrdersCount(takerId,
        // DeliveryOrderContant.CASH_ON_DELIVERY, status, start);
        // viewDeliveryOrderInfos.setTotal_online_count(onLineCount);
        // viewDeliveryOrderInfos.setTotal_offline_count(offLineCount);
        return viewDeliveryOrderInfos;

    }
}
