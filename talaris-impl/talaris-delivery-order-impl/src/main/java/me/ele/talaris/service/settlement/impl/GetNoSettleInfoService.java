package me.ele.talaris.service.settlement.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.ele.talaris.constant.Constant;
import me.ele.talaris.deliveryorder.persistent.dao.DeliveryOrderDao;
import me.ele.talaris.dao.UserDao;
import me.ele.talaris.dao.UserStationRoleDao;
import me.ele.talaris.exception.UserException;
import me.ele.talaris.model.Context;
import me.ele.talaris.model.User;
import me.ele.talaris.model.UserStationRole;
import me.ele.talaris.model.settlement.TakerNoSettleInfo;
import me.ele.talaris.model.settlement.TheLastSettleInfo;
import me.ele.talaris.service.deliveryorder.impl.CommonDeliveryOrderService;
import me.ele.talaris.service.settlement.IGetNoSettleInfoService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * 获取站点下人员的未结算信息
 * 
 * @author zhengwen
 *
 */
@Service
public class GetNoSettleInfoService implements IGetNoSettleInfoService {
    public static final Logger logger = LoggerFactory.getLogger(GetNoSettleInfoService.class);
    @Autowired
    SettlementComponent settlementComponent;
    @Autowired
    UserStationRoleDao userStationRoleDao;
    @Autowired
    UserDao userDao;
    @Autowired
    DeliveryOrderDao deliveryOrderDao;

    @Autowired
    CommonDeliveryOrderService commonDeliveryOrderService;

    /**
     * 查询站点下所有配送员未结款信息
     * 
     * @param stationId
     * @return
     * @throws UserException
     */
    @Override
    public List<TakerNoSettleInfo> getNoSettleInfoByStationId(Context context, int stationId, int roleId)
            throws UserException {
        // 该站点下不存在结算记录，则需要初始化
        List<User> userList = new ArrayList<User>();
        List<User> removedUsers = new ArrayList<User>();
        List<UserStationRole> userStationRoleList = userStationRoleDao
                .listUserStationRoleByRoldIdAndStationIdNoMatterStatus(roleId, stationId);
        if (CollectionUtils.isEmpty(userStationRoleList)) {
            return null;
        }
        if (!settlementComponent.isExistSettleInfoByStationId(stationId)) {
            try {
                settlementComponent.initSettleInfoByStationId(context, userStationRoleList);
            } catch (UserException e) {
                logger.error("初始化站点配送人员的结算信息异常", e);
            }
        }

        for (UserStationRole userStationRole : userStationRoleList) {
            User user = userDao.getUserByIdNoMatterStatus(userStationRole.getUser_id());
            logger.debug(user.toString());
            if (user.getStatus() == 0) {
                continue;
            }
            if (userStationRole.getStatus() == 1 && userStationRole.getRole_id() == Constant.COURIER_CODE) { // 该人没有被删除且为配送员
                userList.add(user);
            }
            if (userStationRole.getStatus() == 0 && userStationRole.getRole_id() == Constant.COURIER_CODE) {
                removedUsers.add(user);
            }
        }
        if (CollectionUtils.isEmpty(userList) && CollectionUtils.isEmpty(removedUsers)) {
            return null;
        }
        List<TakerNoSettleInfo> takerNoSettleInfos = new ArrayList<TakerNoSettleInfo>();
        int eleRstId = commonDeliveryOrderService.getEleRestaurantIdByContext(context);
        for (User user : userList) {

            // Timestamp lastSettleTime = settlementComponent.getTheLastSettleTimeByTakerId(user.getId(), context)
            // .getSettleTime();

            // settlementComponent.systemMarkException(context, user.getId(), lastSettleTime);

            TheLastSettleInfo theLastSettleInfo = settlementComponent.getTheLastSettleTimeByTakerId(user.getId(),
                    context, eleRstId);
            logger.debug("餐厅号为：{}", eleRstId);
            logger.debug("time为：{}", theLastSettleInfo.getLastSettleOfflineCash());

            Map<String, Object> result = deliveryOrderDao.getTakerNoSettleCount(user, eleRstId,
                    theLastSettleInfo.getSettleTime());
            TakerNoSettleInfo takerNoSettleInfo = new TakerNoSettleInfo(user.getId(), user.getName(), new BigDecimal(
                    String.valueOf(result.get("no_settle_count") == null ? 0 : result.get("no_settle_count"))),
                    user.getMobile(), 1);
            takerNoSettleInfos.add(takerNoSettleInfo);
        }
        for (User user : removedUsers) {
            // Timestamp lastSettleTime = settlementComponent.getTheLastSettleTimeByTakerId(user.getId(), context)
            // .getSettleTime();

            // settlementComponent.systemMarkException(context, user.getId(), lastSettleTime);

            TheLastSettleInfo theLastSettleInfo = settlementComponent.getTheLastSettleTimeByTakerId(user.getId(),
                    context, eleRstId);

            logger.debug("餐厅号为：{}", eleRstId);
            logger.debug("time为：{}", theLastSettleInfo.getLastSettleOfflineCash());
            Map<String, Object> result = deliveryOrderDao.getTakerNoSettleCount(user, eleRstId,
                    theLastSettleInfo.getSettleTime());
            TakerNoSettleInfo takerNoSettleInfo = new TakerNoSettleInfo(user.getId(), user.getName(), new BigDecimal(
                    String.valueOf(result.get("no_settle_count") == null ? 0 : result.get("no_settle_count"))),
                    user.getMobile(), 0);
            takerNoSettleInfos.add(takerNoSettleInfo);
        }
        return takerNoSettleInfos;
    }

}
