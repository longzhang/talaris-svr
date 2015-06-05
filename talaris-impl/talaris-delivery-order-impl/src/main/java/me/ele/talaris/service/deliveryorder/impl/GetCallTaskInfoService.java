package me.ele.talaris.service.deliveryorder.impl;

import java.util.ArrayList;
import java.util.List;

import me.ele.talaris.dao.CallTaskInfoDao;
import me.ele.talaris.model.CallTaskInfo;
import me.ele.talaris.model.CallTaskInfoEx;
import me.ele.talaris.redis.RedisClient;
import me.ele.talaris.redis.RedisKeys;
import me.ele.talaris.service.deliveryorder.IGetCallTaskInfoService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * 获取电话回调状态
 * 
 * @author zhengwen
 *
 */
@Service
public class GetCallTaskInfoService implements IGetCallTaskInfoService {
    private final static Logger logger = LoggerFactory.getLogger(GetCallTaskInfoService.class);

    @Autowired
    RedisClient client;
    @Autowired
    CallTaskInfoDao callTaskInfoDao;
    @Autowired
    DeliveryOrderComponent deliveryOrderComponent;

    /**
     * 根据电话通知taskID来获取该电话的状态
     * 
     * @param taskIdList
     * @return
     */
    @Override
    public List<CallTaskInfoEx> getCallTaskInfosByIdList(List<Long> taskIdList) {
        List<CallTaskInfo> callTaskInfos = new ArrayList<CallTaskInfo>();
        // 新需求，1分钟后如果还没有回调状态，则直接置为拨打失败
        for (Long taskId : taskIdList) {
            CallTaskInfo callTaskInfo;
            try {
                callTaskInfo = client.hgetObject(RedisKeys.CALL_TASK_INFO_KEY.getKey(), String.valueOf(taskId),
                        CallTaskInfo.class);
                if (System.currentTimeMillis() - callTaskInfo.getCreated_at().getTime() > 70 * 1000
                        && callTaskInfo.getStatus() == 0) {
                    callTaskInfo.setStatus(3);
                }
            }
            // 这里如果是抛出异常，则直接去读取库
            catch (Throwable e) {
                logger.info("从redis里面取电话回调状态失败", e);
                callTaskInfo = callTaskInfoDao.getCallTaskInfoByTaskId(taskId);
                if (System.currentTimeMillis() - callTaskInfo.getCreated_at().getTime() > 70 * 1000) {
                    callTaskInfo.setStatus(3);
                }
            }
            callTaskInfos.add(callTaskInfo);
        }
        deliveryOrderComponent.transformCallStatus(callTaskInfos);
        List<CallTaskInfoEx> callTaskInfoExs = changeCallTaskInfoToEx(callTaskInfos);
        return callTaskInfoExs;
    }

    private List<CallTaskInfoEx> changeCallTaskInfoToEx(List<CallTaskInfo> callTaskInfos) {
        List<CallTaskInfoEx> callTaskInfoExs = new ArrayList<CallTaskInfoEx>();
        if (CollectionUtils.isEmpty(callTaskInfos)) {
            return null;
        }
        for (CallTaskInfo callTaskInfo : callTaskInfos) {
            callTaskInfoExs.add(new CallTaskInfoEx(callTaskInfo));
        }
        return callTaskInfoExs;
    }
}
