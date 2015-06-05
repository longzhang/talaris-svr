package me.ele.talaris.service.settlement.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import me.ele.talaris.constant.Constant;
import me.ele.talaris.deliveryorder.dto.DeliveryOrder;
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
import me.ele.talaris.model.SettlementDeliveryOrder;
import me.ele.talaris.model.StationRestaurant;
import me.ele.talaris.model.User;
import me.ele.talaris.model.UserStationRole;
import me.ele.talaris.model.settlement.TheLastSettleInfo;
import me.ele.talaris.response.DeliveryOrderStatistics;
import me.ele.talaris.service.auth.IUserDeviceService;
import me.ele.talaris.service.deliveryorder.impl.CommonDeliveryOrderService;
import me.ele.talaris.service.deliveryorder.impl.DeliveryOrderComponent;
import me.ele.talaris.service.permission.IPermissionService;
import me.ele.talaris.service.statistics.IStatisticsService;
import me.ele.talaris.service.user.IUserStationRoleService;
import me.ele.talaris.utils.Times;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * @author shaorongfei
 *
 * 公用组件
 */
public class SettlementComponent {
    private SettlementDeliveryOrderDao settlementDeliveryOrderDao;
    private SettlementDao settlementDao;
    private UserDao userDao;
    private DeliveryOrderDao deliveryOrderDao;
    private IStatisticsService statisticsService;
    private IUserStationRoleService userStationRoleService;
    private IPermissionService permitionValidateService;
    private DeliveryOrderComponent deliveryOrderComponent;
    private UserStationRoleDao userStationRoleDao;
    private IUserDeviceService userDeviceService;
    private StationRestaurantDao stationRestaurantDao;
    private RestaurantDao restaurantDao;
    private int expiredHours;
    private CommonDeliveryOrderService commonDeliveryOrderService;

    public static Logger logger = LoggerFactory.getLogger(SettlementComponent.class);

    public void setRestaurantDao(RestaurantDao restaurantDao) {
        this.restaurantDao = restaurantDao;
    }

    public void setStationRestaurantDao(StationRestaurantDao stationRestaurantDao) {
        this.stationRestaurantDao = stationRestaurantDao;
    }

    public CommonDeliveryOrderService getCommonDeliveryOrderService() {
        return commonDeliveryOrderService;
    }

    public void setCommonDeliveryOrderService(CommonDeliveryOrderService commonDeliveryOrderService) {
        this.commonDeliveryOrderService = commonDeliveryOrderService;
    }

    public SettlementDeliveryOrderDao getSettlementDeliveryOrderDao() {
        return settlementDeliveryOrderDao;
    }

    public void setSettlementDeliveryOrderDao(SettlementDeliveryOrderDao settlementDeliveryOrderDao) {
        this.settlementDeliveryOrderDao = settlementDeliveryOrderDao;
    }

    public SettlementDao getSettlementDao() {
        return settlementDao;
    }

    public void setSettlementDao(SettlementDao settlementDao) {
        this.settlementDao = settlementDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public DeliveryOrderDao getDeliveryOrderDao() {
        return deliveryOrderDao;
    }

    public void setDeliveryOrderDao(DeliveryOrderDao deliveryOrderDao) {
        this.deliveryOrderDao = deliveryOrderDao;
    }

    public IStatisticsService getStatisticsService() {
        return statisticsService;
    }

    public void setStatisticsService(IStatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    public IUserStationRoleService getUserStationRoleService() {
        return userStationRoleService;
    }

    public void setUserStationRoleService(IUserStationRoleService userStationRoleService) {
        this.userStationRoleService = userStationRoleService;
    }

    public IPermissionService getPermitionValidateService() {
        return permitionValidateService;
    }

    public void setPermitionValidateService(IPermissionService permitionValidateService) {
        this.permitionValidateService = permitionValidateService;
    }

    public DeliveryOrderComponent getDeliveryOrderComponent() {
        return deliveryOrderComponent;
    }

    public void setDeliveryOrderComponent(DeliveryOrderComponent deliveryOrderComponent) {
        this.deliveryOrderComponent = deliveryOrderComponent;
    }

    public UserStationRoleDao getUserStationRoleDao() {
        return userStationRoleDao;
    }

    public void setUserStationRoleDao(UserStationRoleDao userStationRoleDao) {
        this.userStationRoleDao = userStationRoleDao;
    }

    public IUserDeviceService getUserDeviceService() {
        return userDeviceService;
    }

    public void setUserDeviceService(IUserDeviceService userDeviceService) {
        this.userDeviceService = userDeviceService;
    }

    public int getExpiredHours() {
        return expiredHours;
    }

    public void setExpiredHours(int expiredHours) {
        this.expiredHours = expiredHours;
    }

    /**
     * 获取配送员最后结算配送单编号和时间
     * 
     * @param takerId
     * @return
     * @throws UserException
     */

    public TheLastSettleInfo getTheLastSettleTimeByTakerId(int takerId, Context context, int rstId)
            throws UserException {
        logger.info("当前餐厅号为：{}", rstId);
        logger.info("当前餐厅号为：{}", commonDeliveryOrderService.getDefaultRstIds());
        if (String.valueOf(rstId).equals(commonDeliveryOrderService.getDefaultRstIds())) {
            logger.info("到了归集的餐厅");
            TheLastSettleInfo theLastSettleInfo = new TheLastSettleInfo();
            theLastSettleInfo.setDeliveryOrderId(1l);
            theLastSettleInfo.setIsFirstAddDeliveryMan(0);
            theLastSettleInfo.setSettleTime(new Timestamp(System.currentTimeMillis() - 12 * 60 * 60 * 1000l));
            theLastSettleInfo.setLastSettleOfflineCash(new BigDecimal(0));
            return theLastSettleInfo;
        }
        TheLastSettleInfo settleInfo = new TheLastSettleInfo();
        Restaurant restaurant = restaurantDao.getRestaurantByEleId(rstId);
        StationRestaurant stationRestaurant = stationRestaurantDao.getStationRestaurantByRstId(restaurant.getId());
        List<Settlement> list = settlementDao.getSettlementListByTakerId(takerId, stationRestaurant.getStation_id());
        // 判断默认是否已初始化
        if (isFirstInit(list)) {
            settleInfo.setIsFirstAddDeliveryMan(1);
        }
        // 首次添加配送员未结算,最后结算配送单号默认为0，结算时间为用户创建时间
        if (CollectionUtils.isEmpty(list)) {
            // // 通过stationId判断站点下是否有结算纪录
            // List<Settlement> listOfStation =
            // settlementDao.getSettlementListByStationId(stationId);
            // 第一次调用就以管理员角色初始化getTheLastSettleTimeByTakerId根据站点Id初始化站点下所有配送员的结算信息
            // 并初始化2015-5-12
            int roleID = Constant.COURIER_CODE;
            List<UserStationRole> userStationRoleListAll = new ArrayList<UserStationRole>();
            for (int stationId : context.getStationID()) {
                List<UserStationRole> userStationRoleList = userStationRoleDao
                        .listUserStationRoleByRoldIdAndStationIdNoMatterStatus(roleID, stationId);
                userStationRoleListAll.addAll(userStationRoleList);
            }

            if (!CollectionUtils.isEmpty(userStationRoleListAll)) {
                initSettleInfoByStationId(context, userStationRoleListAll);
                return getTheLastSettleTimeByTakerId(takerId, context, rstId);
            }
            // throw
            // ExceptionFactory.newUserException(ExceptionCode.SETTLEMENT_ERROR_130.getCode(),
            // "需要老板登录蜂鸟配送使用结账功能，以便您查看结算纪录");
            // 新添加配送员取用户创建时间
            Timestamp timeStamp = userStationRoleService.getCreateTimeByUserId(takerId);
            settleInfo.setSettleTime(timeStamp);
            settleInfo.setDeliveryOrderId(0l);
            settleInfo.setLastSettleOfflineCash(new BigDecimal(0));
            settleInfo.setIsFirstAddDeliveryMan(1);
            return settleInfo;
        }
        // 非首次最后结算时间从结算表中排序最大得出，最后结算单号从关联表中取出。
        Collections.sort(list);
        BigDecimal initOfflineAllCash = new BigDecimal(0);
        initOfflineAllCash = list.get(0).getOffline_total_money();
        settleInfo.setLastSettleOfflineCash(initOfflineAllCash);
        settleInfo.setSettleTime(list.get(0).getCreated_at());
        logger.info("takerId{},上次结算金额{},上次结算时间{}", takerId, initOfflineAllCash, list.get(0).getCreated_at());
        List<SettlementDeliveryOrder> settlementDeliveryOrder = settlementDeliveryOrderDao
                .getSettlementDeliveryOrderByTakerId(list.get(0).getId(), takerId);
        if (!CollectionUtils.isEmpty(settlementDeliveryOrder)) {
            String[] deliveryOrderIds = settlementDeliveryOrder.get(0).getDelivery_order_list().split(",");
            // 写入关联表时从小到大排序好，这里直接取最后一位作为最后结算单号
            Long theLastDeliveryOrderId = new Long(
                    StringUtils.isBlank(deliveryOrderIds[deliveryOrderIds.length - 1]) ? "0"
                            : deliveryOrderIds[deliveryOrderIds.length - 1]);
            settleInfo.setDeliveryOrderId(theLastDeliveryOrderId);
        } else {
            settleInfo.setDeliveryOrderId(1L);
        }
        return settleInfo;
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
     * 根据站点Id初始化站点下所有配送员的结算信息 并初始化
     * 
     * @param stationId
     * @return
     * @throws UserException
     */
    public boolean initSettleInfoByStationId(Context context, List<UserStationRole> userStationRoleList)
            throws UserException {
        String currentDate = Times.currentYYYY_MM_DD();
        // 初始化配送员的结算信息
        for (UserStationRole userStationRole : userStationRoleList) {
            List<DeliveryOrderStatistics> settleInfoList = statisticsService.getDeliveryOrderByUserIdAndCreateAt(
                    context, userStationRole.getUser_id(), currentDate, "groupBy");
            Settlement settlement = settlementDao.getSettlementByStationIdAndTakerId(userStationRole.getStation_id(),
                    userStationRole.getUser_id());
            // 初始化情况下传入theLastSettleInfo为null，因为没有上次结算信息，如已有初始化信息就不写入
            if (settlement == null) {
                writeSettlemenRelatedtInfo(context, userStationRole, settleInfoList, userStationRole.getUser_id(), 0,
                        null);
            }

            // 此种情况下让配送员token失效，登出（暂时解决前端残留缓存无法同步更新问题）（管理员例外）
            if (userStationRole.getUser_id() != context.getUser().getId()) {
                int result = userDeviceService.updateTokenByUserId(userStationRole.getUser_id());
                if (result > 0) {
                    logger.info("配送员Id{}结算初始化登出,context.getUser().getId():", userStationRole.getUser_id(), context
                            .getUser().getId());
                }
            }
        }
        return true;
    }

    /**
     * 写入结算相关表信息
     * 
     * @param context
     * @param userStationRole
     * @param settleInfoList
     * @throws UserException
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ, rollbackFor = Exception.class)
    public void writeSettlemenRelatedtInfo(Context context, UserStationRole userStationRole,
            List<DeliveryOrderStatistics> settleInfoList, int takerId, int stationId,
            TheLastSettleInfo theLastSettleInfo) throws SystemException {
        // 构造结算表信息并写入
        int initNormalCount = 0, initOfflineCount = 0, initAbnormalCount = 0;
        BigDecimal initNormalCash = new BigDecimal("0");
        BigDecimal initOfflineCash = new BigDecimal("0");
        BigDecimal initAbnormalCash = new BigDecimal("0");
        // (已成功送达订单 配送中订单 异常订单 钱(只餐到付款算钱) 单量(在线支付和餐到付款都算) 在线支付金额为0)
        for (DeliveryOrderStatistics deliveryOrderStatistics : settleInfoList) {
            if (userStationRole == null && deliveryOrderStatistics.getStatus() == 2) {
                throw ExceptionFactory.newSystemException(ExceptionCode.SETTLEMENT_ERROR_130.getCode(),
                        "存在配送中订单，请置为送达后再结算");
            }
            // 正常订单(已送达订单)
            if (deliveryOrderStatistics.getStatus() == 3) {
                initNormalCount = deliveryOrderStatistics.getCount();
                initNormalCash = initNormalCash.add(deliveryOrderStatistics.getSum());
            }
            // 餐到付款(已送达订单)1.0.6版本算上异常订单餐到付款金额和数量
            logger.debug("支付类型{},配送单状态{}", deliveryOrderStatistics.getPayment_type(),
                    deliveryOrderStatistics.getStatus());
            if (deliveryOrderStatistics.getPayment_type() == 0
                    && (deliveryOrderStatistics.getStatus() == 3 || deliveryOrderStatistics.getStatus() == -1)) {
                initOfflineCount += deliveryOrderStatistics.getCount();
                initOfflineCash = initOfflineCash.add(deliveryOrderStatistics.getSum());
                logger.debug("餐到付款{}", initOfflineCount);
            }
            // 异常订单
            if (deliveryOrderStatistics.getStatus() == -1) {
                initAbnormalCount = deliveryOrderStatistics.getCount();
                initAbnormalCash = initAbnormalCash.add(deliveryOrderStatistics.getSum());
            }
        }
        Date date = new Date();
        Timestamp timeStamp = new Timestamp(date.getTime());
        int operatorId = context.getUser().getId();
        String operatorName = context.getUser().getName();
        String takerName = context.getUser().getName();
        // 区别初始化下多个配送员的userId和正常初始化userid
        int userid = 0;
        // 初始化
        if (userStationRole != null) {
            userid = userStationRole.getUser_id();
        }// 正常结算
        else {
            userid = takerId;
        }
        User user = userDao.getUserById(userid);
        if (user != null) {
            takerName = user.getName();
        }
        // 非初始化结账情况下
        if (initNormalCount == 0 && initOfflineCount == 0 && initAbnormalCount == 0 && userStationRole == null) {
            throw ExceptionFactory.newSystemException(ExceptionCode.SETTLEMENT_ERROR_130.getCode(), "此配送员无可结账信息，无需结账");
        }
        List<DeliveryOrder> theNextDeliveryOrderList = null;
        int status = 0;
        // 正常结算情况
        if (theLastSettleInfo != null) {
            status = 1;
            theNextDeliveryOrderList = deliveryOrderDao.getTheNextDeliveryOrderList(takerId,
                    theLastSettleInfo.getSettleTime());
        }
        // 初始化结算情况
        else {
            theNextDeliveryOrderList = deliveryOrderDao.getInitDeliveryOrderList(takerId, Times.currentYYYY_MM_DD());
            // 初始化结算时间表纪录为当日凌晨零点时间
            timeStamp = new Timestamp(Times.dateFromYYYY_MM_DD(Times.currentYYYY_MM_DD()).getTime());
        }
        if (userStationRole != null) {
            stationId = userStationRole.getStation_id();
        }
        Settlement settlement = new Settlement(operatorId, operatorName, takerId, takerName, stationId,
                initNormalCount, initNormalCash, initOfflineCount, initOfflineCash, initAbnormalCount,
                initAbnormalCash, timeStamp, timeStamp, status);
        settlementDao.addSettlement(settlement);

        // 构造结算关联表信息并写入
        StringBuilder deliveryOrderListString = new StringBuilder();

        Collections.sort(theNextDeliveryOrderList);
        for (DeliveryOrder deliveryOrder : theNextDeliveryOrderList) {
            deliveryOrderListString.append(deliveryOrder.getId() + ",");
        }
        SettlementDeliveryOrder settlementDeliveryOrder = new SettlementDeliveryOrder(settlement.getId(), takerId,
                deliveryOrderListString.toString(), status, timeStamp, timeStamp);
        settlementDeliveryOrderDao.addSettlementDelivery(settlementDeliveryOrder);
    }

    /**
     * 彻底删除结算初始化记录
     * 
     * @param userId
     * @param userName
     * @param stationId
     */
    // @Transactional(isolation = Isolation.REPEATABLE_READ)
    // public void writeToSettlementForDeletedMan(int userId, String userName, int stationId) {
    // Date date = new Date();
    // Timestamp timeStamp = new Timestamp(date.getTime());
    // Settlement settlement = new Settlement(userId, userName, userId, userName, stationId, 0, BigDecimal.ZERO, 0,
    // BigDecimal.ZERO, 0, BigDecimal.ZERO, timeStamp, timeStamp, -1);
    // settlementDao.addSettlement(settlement);
    // SettlementDeliveryOrder settlementDeliveryOrder = new SettlementDeliveryOrder(settlement.getId(), userId,
    // "彻底删除人员结算初始化", -1, timeStamp, timeStamp);
    // settlementDeliveryOrderDao.addSettlementDelivery(settlementDeliveryOrder);
    // }

    /**
     * 根据站点查询是否存在结算纪录
     * 
     * @param stationId
     * 站点Id
     * @return
     */
    public boolean isExistSettleInfoByStationId(int stationId) {
        List<Settlement> list = settlementDao.getSettlementListByStationId(stationId);
        if (CollectionUtils.isEmpty(list)) {
            return false;
        }
        return true;
    }

    /**
     * 根据takerId查询是否存在结算纪录
     * 
     * @param takerId
     * @return
     */
    public List<Settlement> getSettlementListByTakerId(int takerId) {
        List<Settlement> list = settlementDao.getSettlementListByTakerId(takerId);
        return list;
    }

    /**
     * 通过context判断rstId是否属于此配送员，简单校验
     * 
     * @param context
     * @param rstId
     * @return
     * @throws UserException
     */
    public void isRestaurantLegal(Context context, int rstId) throws UserException {
        Restaurant restaurant = restaurantDao.getRestaurantByEleId(rstId);
        if (restaurant == null) {
            throw ExceptionFactory.newUserException(ExceptionCode.SETTLEMENT_ERROR_131.getCode(), "餐厅不存在");
        }
        StationRestaurant stationRestaurant = stationRestaurantDao.getStationRestaurantByRstId(restaurant.getId());
        if (stationRestaurant == null) {
            throw ExceptionFactory.newUserException(ExceptionCode.SETTLEMENT_ERROR_131.getCode(), "餐厅不存在");
        } else {
            if (!context.getStationID().contains(stationRestaurant.getStation_id())) {
                throw ExceptionFactory.newUserException(ExceptionCode.SETTLEMENT_ERROR_132.getCode(), "餐厅不合法");
            }
        }
    }
    // /**
    // * 系统自动标记异常，就是在汇总页面自动将超时的配送中的订单标记为异常
    // *
    // * @param taker_id
    // * @param lastSettleTime
    // * @return
    // * @throws UserException
    // */
    // @Transactional
    // public boolean systemMarkException(Context context, int taker_id,
    // Timestamp lastSettleTime) throws UserException {
    // boolean canWatch = validateCanWatchDeliveryOrders(context, taker_id);
    // if (!canWatch) {
    // new SystemException("USER_OPERATOR_EXCEPTION", "用户操作异常");
    // }
    // List<DeliveryOrder> deliveryingOrders =
    // deliveryOrderDao.selectDeliveryingOrdersInExpiredHours(expiredHours,
    // taker_id, lastSettleTime);
    // if (CollectionUtils.isEmpty(deliveryingOrders)) {
    // logger.debug("4个小时之前为空");
    // return true;
    // }
    // logger.debug("开始系统标记异常");
    // // 配送员自己查看
    // if (context.getUser().getId() == taker_id) {
    // logger.debug("配送员自己");
    // for (DeliveryOrder deliveryOrder : deliveryingOrders) {
    // deliveryOrderComponent.changeDeliveryOrderStatus(context, deliveryOrder,
    // DeliveryOrderContant.EXCEPTION, false,
    // DeliveryOrderContant.MARKEXCEPTIONMSG);
    // }
    // }
    //
    // else {
    // logger.debug("老板");
    // User user = userDao.getUserById(taker_id);
    // if (user == null) {
    // return true;
    // }
    // List<UserStationRole> userStationRoles =
    // userStationRoleDao.listUserStationRoleByUserId(taker_id);
    // Context context2 = new Context(user, userStationRoles);
    // for (DeliveryOrder deliveryOrder : deliveryingOrders) {
    // deliveryOrderComponent.changeDeliveryOrderStatus(context2, deliveryOrder,
    // DeliveryOrderContant.EXCEPTION, false,
    // DeliveryOrderContant.MARKEXCEPTIONMSG);
    // }
    // }
    // return true;
    //
    // }

    // /**
    // * 校验是否能查看相应的配送单信息
    // *
    // * @param context
    // * @param takerId
    // * @return
    // */
    // private boolean validateCanWatchDeliveryOrders(Context context, int
    // takerId) {
    // int userId = context.getUser().getId();
    // if (userId == takerId) {
    // return true;
    // }
    // if (!permitionValidateService.hasRole(context, Constant.STATIONMANAGER))
    // {
    // return false;
    // }
    // // Set<Integer> stationID = context.getStationID();
    // List<Integer> userIDs =
    // userStationRoleDao.listUserIdByStationId(ContextUtil.get);
    // if (CollectionUtils.isEmpty(userIDs)) {
    // return false;
    // }
    // for (Integer userID : userIDs) {
    // if (userID == takerId) {
    // return true;
    // }
    // }
    // return false;
    // }

}
