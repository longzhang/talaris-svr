package me.ele.talaris.napos.getorder.task;

import java.util.ArrayList;
import java.util.List;

import me.ele.talaris.dao.RestaurantDao;
import me.ele.talaris.deliveryorder.persistent.dao.DeliveryOrderDao;
import me.ele.talaris.errorlog.processor.ErrorLogProcessor;
import me.ele.talaris.exception.UserException;
import me.ele.talaris.log.holder.LogHolder;
import me.ele.talaris.model.Context;
import me.ele.talaris.model.User;
import me.ele.talaris.model.UserStationRole;
import me.ele.talaris.napos.model.TEleOrder;
import me.ele.talaris.napos.wrapper.NaposServiceWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetOrderFromNaposTask implements InitializingBean {
    public GetOrderFromNaposTask() {
        super();
    }

    public static final Logger LOGGER = LoggerFactory.getLogger(GetOrderFromNaposTask.class);
    @Autowired
    private NaposServiceWrapper naposServiceWrapper;
    @Autowired
    private RestaurantDao restaurantDao;
    @Autowired
    private DeliveryOrderDao deliveryOrderDao;
    @Autowired
    ErrorLogProcessor errorLogProcessor;
    @Autowired
    GetRestaurantModule getRestaurantModule;
    @Autowired
    TransformOrder transformOrder;

    private Context context = new Context();

    @Override
    public void afterPropertiesSet() throws Exception {
        /**
         * 这里虚拟出来一个用户也就是系统。
         */
        User user = new User();
        user.setMobile(4000557117l);
        user.setId(0);
        user.setName("SYSTEM");
        UserStationRole userStationRole = new UserStationRole();
        userStationRole.setStation_id(0);
        List<UserStationRole> userStationRoles = new ArrayList<UserStationRole>();
        userStationRoles.add(userStationRole);
        context.setUser(user);
        context.setUserStationRoles(userStationRoles);
    }

    public void startTask() {
        int rstIdCount = 0;
        try {
            rstIdCount = Integer.valueOf(String.valueOf(restaurantDao.selectRstIdCount()));
            LogHolder.bunchLog("当前餐厅数量为：" + rstIdCount + ";");
        } catch (Exception e) {
            LOGGER.error("获取餐厅总数异常", e);
        }
        List<Integer> result = getRestaurantModule.getRestaurantIds("KEY", rstIdCount);

        if (result == null) {
            return;
        }
        List<Integer> rstIdsModule = null;
        if (result.get(0) != 1) {
            rstIdsModule = restaurantDao.getRstModule((result.get(0) - 1) * result.get(1) - 1, result.get(1));
        } else {
            rstIdsModule = restaurantDao.getRstModule(0, result.get(1));
        }
        if (rstIdsModule == null) {
            return;
        }
        LogHolder.bunchLog("当前拉取的餐厅号有：" + changerListToString(rstIdsModule) + ";");
        for (Integer rstID : rstIdsModule) {

            List<TEleOrder> tEleOrders = null;
            try {
                tEleOrders = naposServiceWrapper.requestGetEleOrderListByRestaurantId(rstID);
            } catch (UserException e) {
                LOGGER.error("餐厅拉取订单失败" + rstID, e);
                LogHolder.bunchLog("餐厅拉取订单失败:" + rstID + ";");
                errorLogProcessor.writeErrorInfo("cache module fetch order fail ,rst_id=" + rstID);
            }
            transformOrder.process(context, tEleOrders, rstID);

        }
        LOGGER.info(LogHolder.getLog().toString());
        LogHolder.clear();

    }

    private String changerListToString(List<Integer> rstIds) {
        StringBuilder stringBuilder = new StringBuilder();
        int count = rstIds.size();
        for (int i = 0; i < count; i++) {
            if (i != count - 1) {
                stringBuilder.append(rstIds.get(i)).append(",");
            } else {
                stringBuilder.append(rstIds.get(i));
            }
        }
        return stringBuilder.toString();
    }
}
