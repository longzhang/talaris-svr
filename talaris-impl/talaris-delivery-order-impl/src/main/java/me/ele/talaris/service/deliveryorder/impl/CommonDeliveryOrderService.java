package me.ele.talaris.service.deliveryorder.impl;

import java.util.HashSet;
import java.util.Set;

import me.ele.talaris.context.utils.ContextUtil;
import me.ele.talaris.dao.StationRestaurantDao;
import me.ele.talaris.exception.UserException;
import me.ele.talaris.model.Context;
import me.ele.talaris.model.StationRestaurant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
public class CommonDeliveryOrderService implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(CommonDeliveryOrderService.class);
    @Autowired
    StationRestaurantDao stationRestaurantDao;

    @Value("${default.rstIds}")
    String defaultRstIds;
    // 非饿了么的单子关联的餐厅号（方便扩展，做成复数）
    private Set<Integer> rstIds = new HashSet<Integer>();

    public Set<Integer> getRstIds() {
        return rstIds;
    }

    public void setRstIds(Set<Integer> rstIds) {
        this.rstIds = rstIds;
    }

    public String getDefaultRstIds() {
        return defaultRstIds;
    }

    public void setDefaultRstIds(String defaultRstIds) {
        this.defaultRstIds = defaultRstIds;
    }

    /**
     * 根据context查看自己对应了多少餐厅
     * 
     * @param context
     * @return
     */
    public Set<Integer> getRstIdsByContext(Context context) {
        Set<Integer> rstIds = new HashSet<Integer>();
        Set<Integer> stationIds = context.getStationID();
        for (Integer stationId : stationIds) {
            int restaurantId = stationRestaurantDao.getEleRestaurantIdByStaionId(stationId);
            rstIds.add(restaurantId);
        }
        if (!CollectionUtils.isEmpty(this.rstIds)) {
            rstIds.addAll(this.rstIds);
        }
        return rstIds;

    }

    /**
     * 根据rstId查询station信息
     * 
     * @param rstId
     * @return
     */
    public StationRestaurant getStationRestaurantByRstId(int rstId) {
        return stationRestaurantDao.getStationRestaurantByRstId(rstId);

    }

    public int getEleRestaurantIdByContext(Context context) throws UserException {
        return stationRestaurantDao.getEleRestaurantIdByStaionId(ContextUtil.getMyStationIdByContext(context));

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (StringUtils.isEmpty(defaultRstIds)) {
            return;
        }
        String[] ss = defaultRstIds.split(",");
        for (String s : ss) {
            logger.info("当前配置的餐厅有：{}", s);
            rstIds.add(Integer.valueOf(s));
        }
    }
}
