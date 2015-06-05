package me.ele.talaris.hermes.wrapper;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import me.ele.talaris.constant.Constant;
import me.ele.talaris.dao.CallTaskInfoDao;
import me.ele.talaris.exception.SystemException;
import me.ele.talaris.exception.UserException;
import me.ele.talaris.hermes.client.AudioTaskCreationParameter;
import me.ele.talaris.hermes.client.HermesErrorCode;
import me.ele.talaris.hermes.client.HermesService;
import me.ele.talaris.hermes.client.HermesUserException;
import me.ele.talaris.hermes.client.NormalTaskCreationParameter;
import me.ele.talaris.hermes.client.THermesAudioVerifyCodeCallType;
import me.ele.talaris.hermes.client.TemplateTaskCreationParameter;
import me.ele.talaris.hermes.client.VerifyCodeCreationParameter;
import me.ele.talaris.hermes.client.VerifyCodeCreationResult;
import me.ele.talaris.hermes.client.HermesService.Client;
import me.ele.talaris.model.CallTaskInfo;
import me.ele.talaris.redis.RedisClient;
import me.ele.talaris.redis.RedisKeys;
import me.ele.talaris.utils.SerializeUtil;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.mysql.jdbc.StringUtils;

/**
 * @author kimizhang
 *
 */
public class HermesServiceWrapper {

    private final static Logger logger = LoggerFactory.getLogger(HermesServiceWrapper.class);

    private String host;
    private int port;
    private String verifyCodeSenderKey;
    private short expire;

    private CallTaskInfoDao callTaskInfoDao;

    private RedisClient redisClient;

    public RedisClient getRedisClient() {
        return redisClient;
    }

    public void setRedisClient(RedisClient redisClient) {
        this.redisClient = redisClient;
    }

    private String smsSenderKey;
    private boolean need_reply;
    private short retry;
    private String sendDeliveringNotificationSlug;
    private String sendDeliveringNotificationWithUrlSlug;
    private String sendAppDownloadUrlSlug;
    private String restaurantBindNotificationSlug;
    private String sendDeliveredNotificationSlug;
    private String notifyPhoneNumber;

    /**
     * 给number发送内容为content的短信
     * 
     * @param number
     * @param content
     * @return 返回 taskId 用于查询发送状态
     * @throws UserException
     * @throws SystemException
     */
    public long sendMessage(String number, String content) throws UserException {
        HermesService.Client client = null;
        long taskId = 0;
        try {
            client = getClient();
            NormalTaskCreationParameter param = new NormalTaskCreationParameter();
            param.setReceivers(number);
            param.setMessage(content);
            param.setSender_key(smsSenderKey);
            param.setNeed_reply(need_reply);
            param.setRetry_(retry);
            taskId = client.create_task(param);
        } catch (Exception e) {
            logger.error("sendMessage failed, mobile:{}, content:{}", number, content, e);
            throw new UserException("DELIVERY_ORDER_ERROR_500", "通知用户失败");
        } finally {
            closeClient(client);
        }
        return taskId;
    }

    public VerifyCodeCreationResult requestSendVerifyCodeUseVoice(String mobileNumber) throws UserException {
        HermesService.Client client = null;
        VerifyCodeCreationResult result = null;
        try {
            client = getClient();
            VerifyCodeCreationParameter parameter = new VerifyCodeCreationParameter();
            parameter.setSender_key(verifyCodeSenderKey);
            parameter.setReceiver(mobileNumber);
            parameter.setExpire(expire);
            parameter.setVia_audio(true);
            parameter.setAudio_call_type(THermesAudioVerifyCodeCallType.OUT);
            result = client.verify_code_create(parameter);
        } catch (Exception e) {
            logger.error("send audio verifycode failed, mobile:{}", mobileNumber, e);
            if (e instanceof HermesUserException) {
                HermesUserException error = (HermesUserException) e;
                if (error.getError_code().getValue() == 15) {
                    throw new UserException("HERMES_ERROR_001", "发送验证码次数超过限制");
                }
            }
            throw new UserException("AUTH_ERROR_504", "发送语音验证码失败");
        } finally {
            closeClient(client);
        }
        return result;
    }

    public VerifyCodeCreationResult requestSendVerifyCode(String mobileNumber) throws UserException {
        HermesService.Client client = null;
        VerifyCodeCreationResult result = null;
        try {
            client = getClient();
            VerifyCodeCreationParameter parameter = new VerifyCodeCreationParameter();
            parameter.setSender_key(verifyCodeSenderKey);
            parameter.setReceiver(mobileNumber);
            parameter.setExpire(expire);
            result = client.verify_code_create(parameter);
        } catch (Exception e) {
            logger.error("send verifycode failed, mobile:{}", mobileNumber, e);
            if (e instanceof HermesUserException) {
                HermesUserException error = (HermesUserException) e;
                logger.error("send limit exceed");
                if (error.getError_code() == HermesErrorCode.VERIFY_CODE_SEND_LIMIT_REACHED) {
                    throw new UserException("HERMES_ERROR_001", "发送验证码次数超过限制");
                }
            }
            throw new UserException("AUTH_ERROR_505", "发送短信验证码失败");
        } finally {
            closeClient(client);
        }
        return result;
    }

    public long requestSendAudioMessage(String mobileNumber, String content, long deliveryOrderId) throws UserException {
        HermesService.Client client = null;
        long result = 0;
        try {
            client = getClient();
            AudioTaskCreationParameter parameter = new AudioTaskCreationParameter();
            parameter.setSender_key(smsSenderKey);
            parameter.setReceivers(mobileNumber);
            parameter.setMessage(content);
            parameter.setRetry_(retry);
            result = client.create_audio_task(parameter);
            if (deliveryOrderId > 0) {
                CallTaskInfo callTaskInfo = new CallTaskInfo();
                callTaskInfo.setTask_id(result);
                callTaskInfo.setDelivery_order_id(deliveryOrderId);
                callTaskInfo.setStatus(0);
                callTaskInfo.setError_log("");
                callTaskInfo.setCreated_at(new Timestamp(System.currentTimeMillis()));
                callTaskInfo.setUpdate_time(Integer.parseInt(String.valueOf((System.currentTimeMillis() / 1000))));
                try {
                    redisClient.hset(RedisKeys.CALL_TASK_INFO_KEY.getKey(), String.valueOf(result), callTaskInfo);
                    try {
                        callTaskInfoDao.insert(callTaskInfo);
                    } catch (Exception e) {
                        logger.error("insert callTaskInfo into db failed, taskId:{}", result, e);
                    }
                } catch (Exception e) {
                    logger.error("insert callTaskInfo into redis failed, taskId:{}", result, e);
                    throw new SystemException("HERMES_ERROR_404", "系统异常");
                }
            }
            return result;
        } catch (Exception e) {
            logger.error("send audio message failed, mobile:{}, content:{}", mobileNumber, content, e);
            throw new UserException("DELIVERY_ORDER_ERROR_580", "系统异常，请重试或者手工拨打");
        } finally {
            closeClient(client);
        }
    }

    /**
     * 请求hermes发送短信模版
     * 
     * @param recMobile
     * @param mobile
     * 如果type=1为空
     * @param name
     * @param url
     * 如果type不为1则为空
     * @param type
     * type=0 配送中短信模版 type=1 发送下载链接 type=2 发送餐厅绑定通知 type=3 发送确认送达短信
     * @return
     * @throws UserException
     */

    public long requestSendTemplateMessage(String recMobile, String mobile, String name, String url, int type)
            throws UserException {
        HermesService.Client client = null;
        long result = 0;
        try {
            client = getClient();
            TemplateTaskCreationParameter parameter = new TemplateTaskCreationParameter();
            Map<String, String> templateParams = new HashMap<String, String>();
            long templateId = 0;
            switch (type) {
                case 0:
                    templateId = client.get_template_id_by_slug(sendDeliveringNotificationSlug);
                    templateParams.put("name", name);
                    templateParams.put("mobile", mobile);
                    templateParams.put("phone", notifyPhoneNumber);
                    break;
                case 1:
                    templateId = client.get_template_id_by_slug(sendAppDownloadUrlSlug);
                    templateParams.put("name", name);
                    templateParams.put("mobile", recMobile);
                    templateParams.put("url", url);
                    break;
                case 2:
                    templateId = client.get_template_id_by_slug(restaurantBindNotificationSlug);
                    templateParams.put("name", name);
                    templateParams.put("mobile", mobile);
                    templateParams.put("url", Constant.APPDOWNLOADURL);
                    break;
                case 3:
                    templateId = client.get_template_id_by_slug(sendDeliveredNotificationSlug);
                    templateParams.put("name", name);
                    templateParams.put("mobile", mobile);
                    break;
                case 4:
                	templateId = client.get_template_id_by_slug(sendDeliveringNotificationWithUrlSlug);
                	templateParams.put("name", name);
                    templateParams.put("mobile", mobile);
                    templateParams.put("phone", notifyPhoneNumber);
                    templateParams.put("url", url);
                    break;
            }
            String jTemplateParams = SerializeUtil.beanToJson(templateParams);
            logger.debug("templateParams json:{}", jTemplateParams);
            parameter.setSender_key(smsSenderKey);
            parameter.setReceivers(recMobile);
            parameter.setTemplate_id(templateId);
            parameter.setTemplate_params(jTemplateParams);
            parameter.setNeed_reply(false);
            parameter.setRetry_(retry);
            result = client.create_template_task(parameter);
            return result;
        } catch (Exception e) {
            if (type == 0) {
                logger.error("send delivered notification message failed, mobile:{}", recMobile, e);
                throw new UserException("DELIVERY_ORDER_ERROR_580", "系统异常，请重试或者手工拨打");
            } else {
                logger.error("send app download url message failed, mobile:{}", recMobile, e);
                throw new SystemException("HERMES_ERROR_002", "发送app下载链接失败");
            }
        } finally {
            closeClient(client);
        }
    }

    public boolean validateCodeWithReceiver(String code, String mobileNumber) throws UserException {
        HermesService.Client client = null;
        boolean result = false;
        try {
            client = getClient();
            result = client.validate_verify_code_with_receiver(verifyCodeSenderKey, mobileNumber, code);
        } catch (Exception e) {
            logger.error("validate Code With Receiver failed, code:{}, mobile:{}", code, mobileNumber, e);
            throw new UserException("AUTH_ERROR_506", "验证失败");
        } finally {
            closeClient(client);
        }
        return result;

    }

    public void updateRedisCallTaskInfo(CallTaskInfo callTaskInfo) {
        try {
            redisClient.hset(RedisKeys.CALL_TASK_INFO_KEY.getKey(), String.valueOf(callTaskInfo.getTask_id()),
                    callTaskInfo);
        } catch (Exception e) {
            logger.error("fail to update callTaskInfo into redis, taskId:{}", callTaskInfo.getId(), e);
            throw new SystemException("REDIS_ERROR", "fail to update callTaskInfo");
        }
    }

    public boolean isRedisCallTaskInfoExist(long taskId) {
        try {
            return redisClient.hexists(RedisKeys.CALL_TASK_INFO_KEY.getKey(), String.valueOf(taskId));
        } catch (Exception e) {
            logger.error("fail to hexists call TaskInfo, taskId:{}", taskId, e);
            throw new SystemException("REDIS_ERROR", "fail to hexists callTaskInfo");
        }
    }

    public CallTaskInfo getRedisCallTaskInfo(long taskId) {
        try {
            return redisClient.hgetObject(RedisKeys.CALL_TASK_INFO_KEY.getKey(), String.valueOf(taskId),
                    CallTaskInfo.class);
        } catch (Exception e) {
            logger.error("fail to get call TaskInfo from redis, taskId:{}", taskId, e);
            throw new SystemException("REDIS_ERROR", "fail to get callTaskInfo");
        }
    }

    public void updateDbCallTaskInfo(CallTaskInfo callTaskInfo) {
        try {
            callTaskInfoDao.update(callTaskInfo);
        } catch (Exception e) {
            logger.error("fail to update callTaskInfo into db, taskId:{}", callTaskInfo.getTask_id(), e);
        }
    }

    public CallTaskInfo getDbCallTaskInfo(long taskId) {
        try {
            return callTaskInfoDao.getCallTaskInfoByTaskId(taskId);
        } catch (Exception e) {
            logger.error("faiil to get callTaskInfo from db, taskId:{}", taskId, e);
            throw new SystemException("DATABASE_ERROR", "数据库查询callTaskInfo失败");
        }
    }

    public boolean smsAlarmExceed() {
        try {
            String result = (String) redisClient.getByKey(RedisKeys.SMS_ALARM_KEY.getKey());
            if (StringUtils.isNullOrEmpty(result)) {
                redisClient.incr(RedisKeys.SMS_ALARM_KEY.getKey());
                return false;
            }
            logger.debug("sms alerm key acount:{}", Integer.parseInt(result));
            if (Integer.parseInt(result) >= 10) {
                return true;
            }
            logger.debug("sengding alerm");
            redisClient.incr(RedisKeys.SMS_ALARM_KEY.getKey());
        } catch (Exception e) {
            logger.error("incr sms alerm acount failed", e);
        }
        return false;
    }

    public short getExpire() {
        return expire;
    }

    public void setExpire(short expire) {
        this.expire = expire;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setSenderName(String senderName) {
    }

    public void setVerifyCodeSenderKey(String verifyCodeSenderKey) {
        this.verifyCodeSenderKey = verifyCodeSenderKey;
    }

    public String getVerifyCodeSenderKey() {
        return verifyCodeSenderKey;
    }

    public void setSmsSenderKey(String smsSenderKey) {
        this.smsSenderKey = smsSenderKey;
    }

    public String getSmsSenderKey() {
        return smsSenderKey;
    }

    public void setNeed_reply(boolean need_reply) {
        this.need_reply = need_reply;
    }

    public void setRetry(short retry) {
        this.retry = retry;
    }

    public void setCallTaskInfoDao(CallTaskInfoDao callTaskInfoDao) {
        this.callTaskInfoDao = callTaskInfoDao;
    }

    public void setSendDeliveringNotificationSlug(String sendDeliveringNotificationSlug) {
        this.sendDeliveringNotificationSlug = sendDeliveringNotificationSlug;
    }
    
    public void setSendDeliveringNotificationWithUrlSlug(String sendDeliveringNotificationWithUrlSlug) {
		this.sendDeliveringNotificationWithUrlSlug = sendDeliveringNotificationWithUrlSlug;
	}

	public void setSendAppDownloadUrlSlug(String sendAppDownloadUrlSlug) {
        this.sendAppDownloadUrlSlug = sendAppDownloadUrlSlug;
    }

    public void setRestaurantBindNotificationSlug(String restaurantBindNotificationSlug) {
        this.restaurantBindNotificationSlug = restaurantBindNotificationSlug;
    }

    public void setSendDeliveredNotificationSlug(String sendDeliveredNotificationSlug) {
        this.sendDeliveredNotificationSlug = sendDeliveredNotificationSlug;
    }

    public void setNotifyPhoneNumber(String notifyPhoneNumber) {
        this.notifyPhoneNumber = notifyPhoneNumber;
    }

    private void closeClient(Client client) {
        if (client.getInputProtocol().getTransport().isOpen()) {
            client.getInputProtocol().getTransport().close();
            client = null;
        }
        client = null;
    }

    private Client getClient() throws TTransportException {
        TTransport transport = new TSocket(host, port);
        transport.open();
        TProtocol protocol = new TBinaryProtocol(transport);
        HermesService.Client client = new HermesService.Client(protocol);
        return client;
    }

}
