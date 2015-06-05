package me.ele.talaris.eleorderid.generate;

import java.sql.Timestamp;
import java.util.Calendar;

import me.ele.talaris.redis.CacheLock;
import me.ele.talaris.redis.LockedObject;
import me.ele.talaris.redis.RedisClient;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 非饿了么单子生成单号和流水号
 * @author zhengwen
 *
 */
@Service
public class IdGenerator implements InitializingBean {
    private static final String ELE_ORDER_ID_KEY = "ELEORDERIDKEY";
    private static final String ELE_ORDER_SN_KEY = "ELEORDERSNKEY";
    @Autowired
    RedisClient redisClient;
    private Timestamp today;
    private static long oneday = 24 * 60 * 60 * 1000l;

    public Long getEleOrderId() {
        return redisClient.decr(ELE_ORDER_ID_KEY);
    }

    public int getEleOrderSn() {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        if (currentTime.getTime() - today.getTime() > oneday) {
            // 跨天处理
            initToday();
            return jumpDayProcess("CREATE_ELE_ORDER_SN");
        }
        return Integer.valueOf(String.valueOf(redisClient.incr(ELE_ORDER_SN_KEY)));
    }

    @CacheLock
    public int jumpDayProcess(@LockedObject String lockedKey) {
        // 跨天如果流水号超过5就当做要重新生成sn
        int currentSn = Integer.valueOf(redisClient.getByKey(ELE_ORDER_SN_KEY).toString());
        if (currentSn > 1000) {
            redisClient.set(ELE_ORDER_SN_KEY, "0");
            return 0;
        }
        return Integer.valueOf(String.valueOf(redisClient.incr(ELE_ORDER_SN_KEY)));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initToday();
    }

    private void initToday() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        today = new Timestamp(cal.getTime().getTime());
    }

}
