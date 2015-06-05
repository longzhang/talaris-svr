package me.ele.talaris.napos.getorder.task;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import me.ele.talaris.dao.RestaurantIdsModuleDao;
import me.ele.talaris.errorlog.processor.ErrorLogProcessor;
import me.ele.talaris.log.holder.LogHolder;
import me.ele.talaris.model.RestaurantIdsModule;
import me.ele.talaris.redis.CacheLock;
import me.ele.talaris.redis.LockedObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetRestaurantModule {
    public static final Logger LOGGER = LoggerFactory.getLogger(GetRestaurantModule.class);

    @Autowired
    private ErrorLogProcessor errorLogProcessor;
    @Autowired
    private RestaurantIdsModuleDao restaurantIdsModuleDao;

    /**
     * 这次该拿哪个餐厅块去napos拉取配送单
     * 
     * @return 该result其实是一个返回结构体，第一个值是：当前餐厅块ID，第二个值是：每块多少餐厅数量
     */
    @CacheLock(lockedPrefix = "UPDATE_GET_ORDER_RST_MODULE", timeOut = 3000l, expireTime = 4)
    public List<Integer> getRestaurantIds(@LockedObject String key,int rstIdCount) {
        List<Integer> result = new ArrayList<Integer>();
        RestaurantIdsModule newRestaurantIdsModule = new RestaurantIdsModule();
        try {
            // 该result其实是一个返回结构体，第一个值是：当前餐厅块ID，第二个值是：每块多少餐厅数量

            RestaurantIdsModule restaurantIdsModule = restaurantIdsModuleDao.getEarliestRestaurantIdsModule();

            double b = (double) rstIdCount / restaurantIdsModule.getRst_count_once();
            // 当前餐厅数量能分成多少块，这里采用了进一法
            int pieceCount = new BigDecimal(Math.ceil(b)).intValue();

            int current = 1;
            if (pieceCount <= restaurantIdsModule.getCurrent_module_id()) {
                current = 1;
            } else {
                current = restaurantIdsModule.getCurrent_module_id() + 1;
            }
            newRestaurantIdsModule.setCurrent_module_id(current);
            newRestaurantIdsModule.setCreate_at(new Timestamp(System.currentTimeMillis()));
            newRestaurantIdsModule.setRst_count_once(restaurantIdsModule.getRst_count_once());

            restaurantIdsModuleDao.insertRestaurantIdsModule(newRestaurantIdsModule);
        } catch (Exception e) {
            LOGGER.error("XXXX", e);
            errorLogProcessor.writeErrorInfo(e);
            errorLogProcessor.writeErrorInfo("锁失败");

        }
        LogHolder.bunchLog("当前执行的餐厅块ID为:"+newRestaurantIdsModule.getCurrent_module_id()+";");

        LOGGER.info("当前执行的餐厅块ID为：{}", newRestaurantIdsModule.getCurrent_module_id());
        result.add(newRestaurantIdsModule.getCurrent_module_id());
        result.add(newRestaurantIdsModule.getRst_count_once());
        return result;
    }

}
