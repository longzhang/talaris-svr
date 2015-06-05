package me.ele.talaris.hermes.service.impl;

import me.ele.talaris.exception.SystemException;
import me.ele.talaris.exception.UserException;
import me.ele.talaris.hermes.client.VerifyCodeCreationResult;
import me.ele.talaris.hermes.model.SendVerifyCodeResult;
import me.ele.talaris.hermes.service.IHermesService;
import me.ele.talaris.hermes.wrapper.HermesServiceWrapper;
import me.ele.talaris.model.CallTaskInfo;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author kimizhang
 *
 */

public class CoffeeHermesService implements IHermesService {
	private final static Logger logger = LoggerFactory.getLogger(CoffeeHermesService.class);
	private HermesServiceWrapper hermesServiceWrapper;
	private String devMobileList;

	/**
	 * 给指定手机发送一个语音验证码
	 * 
	 * @param mobile
	 * @return 包含发送的验证码和hash_value
	 * @throws UserException
	 */
	@Override
	public SendVerifyCodeResult requestSendVerifyCodeUseVoice(String mobile) throws UserException {
		try {
			logger.info("手机:{}, 请求发送语音验证码", mobile);
			VerifyCodeCreationResult result = hermesServiceWrapper.requestSendVerifyCodeUseVoice(mobile);
			logger.info("手机:{}, 发送语音验证码成功, 验证码:{}", mobile, result.getCode());
			return new SendVerifyCodeResult().setCode(result.getCode()).setHashValue(result.getHash_value());
		} catch (UserException e) {
			logger.error("发送语音验证码失败 mobile:{}, code:{} , message:{}", mobile, e.getErrorCode(), e.getMessage());
			throw e;
		}
	}

	/**
	 * 给指定手机发送短信验证码
	 * 
	 * @param mobile
	 * @return 包含发送的验证码和hash_value
	 * @throws UserException
	 */
	@Override
	public SendVerifyCodeResult requestSendVerifyCode(String mobile) throws UserException {
		try {
			logger.info("手机:{}, 请求发送短信验证码", mobile);
			VerifyCodeCreationResult result = hermesServiceWrapper.requestSendVerifyCode(mobile);
			logger.info("手机:{}, 发送短信验证码成功, 验证码:{}", mobile, result.getCode());
			return new SendVerifyCodeResult().setCode(result.getCode()).setHashValue(result.getHash_value());
		} catch (UserException e) {
			logger.error("发送短信验证码失败 mobile:{}, code:{} , message:{}", mobile, e.getErrorCode(), e.getMessage());
			throw e;
		}
	}

	/**
	 * 给指定手机发送语音短信
	 * 
	 * @param mobile
	 * @param content
	 * @return taskId
	 * @throws UserException
	 */

	@Override
	public long sendAudioMessage(String mobile, String content, long deliveryOrderId) throws UserException {

		try {
			logger.info("手机:{}, 请求发送语音短信内容为:{}", mobile, content);
			long result = hermesServiceWrapper.requestSendAudioMessage(mobile, content, deliveryOrderId);
			logger.info("发送成功，taskid:{}, mobile:{}, content:{}", result, mobile, content);
			return result;
		} catch (UserException e) {
			logger.error(String.format("发送语音短信失败 mobile:[%s],code : [%s] , message : [%s]", mobile, e.getErrorCode(),
					e.getMessage()));
			throw e;
		}
	}

	/**
	 * 配送中短信通知
	 * 
	 * @param recMobile
	 * @param courierMobile
	 * @param name
	 * @return
	 * @throws UserException
	 */

	@Override
	public long sendDeliveringMessage(String recMobile, String courierMobile, String name) throws UserException {
		try {
			logger.info("手机:{}, 姓名:{}, 请求发送配送中短信, 发送给:{}", courierMobile, name, recMobile);
			long result = hermesServiceWrapper.requestSendTemplateMessage(recMobile, courierMobile, name, "", 0);
			logger.info("发送成功， taskId:{}, mobile:{}", result, recMobile);
			return result;
		} catch (UserException e) {
			logger.error(String.format("发送配送中短信失败 mobile:[%s],code : [%s] , message : [%s]", recMobile,
					e.getErrorCode(), e.getMessage()));
			throw e;
		}
	}
	
	/**
	 * 配送中短信（带url）通知
	 * @param recMobile
	 * @param courierMobile
	 * @param name
	 * @param url
	 * @return
	 * @throws UserException
	 */
	
	public long sendDeliveringMessage(String recMobile, String courierMobile,
			String name, String url) throws UserException {
		try {
			logger.info("手机:{}, 姓名:{}, 请求发送带url配送中短信, url:{}, 发送给:{}", courierMobile, name, url, recMobile);
			long result = hermesServiceWrapper.requestSendTemplateMessage(recMobile, courierMobile, name, url, 4);
			logger.info("发送成功， taskId:{}, mobile:{}", result, recMobile);
			return result;
		} catch (UserException e) {
			logger.error(String.format("发送带url配送中短信失败 mobile:[%s],code : [%s] , message : [%s]", recMobile,
					e.getErrorCode(), e.getMessage()));
			throw e;
		}
	}

	/**
	 * 送达短信通知
	 * 
	 * @param recMobile
	 * @param courierMobile
	 * @param name
	 * @return
	 * @throws UserException
	 */

	@Override
	public long sendDeliveredMessage(String recMobile, String courierMobile, String name) throws UserException {
		try {
			logger.info("手机:{}, 姓名:{}, 请求发送送达短信, 发送给:{}", courierMobile, name, recMobile);
			long result = hermesServiceWrapper.requestSendTemplateMessage(recMobile, courierMobile, name, "", 3);
			logger.info("发送成功， taskId:{}, mobile:{}", result, recMobile);
			return result;
		} catch (UserException e) {
			logger.error(String.format("发送送达短信失败 mobile:[%s],code : [%s] , message : [%s]", recMobile,
					e.getErrorCode(), e.getMessage()));
			throw e;
		}
	}

	/**
	 * 餐厅绑定通知短信
	 * 
	 * @param recMobile
	 * @param loginMobile
	 * @param resName
	 * @return
	 * @throws UserException
	 */

	@Override
	public long sendRestaurantBindNotification(String recMobile, String loginMobile, String resName)
			throws UserException {
		try {
			logger.info("发送餐厅绑定通知短信, 餐厅:{}, 发送给:{}", resName, recMobile);
			long result = hermesServiceWrapper.requestSendTemplateMessage(recMobile, loginMobile, resName, "", 2);
			logger.info("发送成功， taskId:{}, mobile:{}", result, recMobile);
			return result;
		} catch (UserException e) {
			logger.error(String.format("发送餐厅绑定通知失败 mobile:[%s],code : [%s] , message : [%s]", recMobile,
					e.getErrorCode(), e.getMessage()));
			throw e;
		}
	}

	/**
	 * 发送App下载链接
	 * 
	 * @param mobile
	 * @param name
	 * @param url
	 * @return taskId
	 * @throws SystemException
	 */

	@Override
	public long sendAppDownloadUrlMessage(String mobile, String name, String url) throws UserException {
		try {
			logger.info("餐厅:{}, 请求发送app下载链接短信, 发送给:{}", name, mobile);
			long result = hermesServiceWrapper.requestSendTemplateMessage(mobile, "", name, url, 1);
			logger.info("发送成功， taskId:{}, mobile:{}", result, mobile);
			return result;
		} catch (UserException e) {
			logger.error(String.format("发送app下载链接短信失败 mobile:[%s],code : [%s] , message : [%s]", mobile,
					e.getErrorCode(), e.getMessage()));
			throw e;
		}
	}

	/**
	 * 异常报警
	 * 
	 * @param content
	 * @param type
	 *            大于0表示严重警报，发送语音通知，小于0只发送短信
	 */

	@Override
	public void safeSendAlarm(String content, int type) {
		if (StringUtils.isEmpty(devMobileList)) {
			return;
		}
		if (hermesServiceWrapper.smsAlarmExceed()) {
			return;
		}
		String[] mobiles = devMobileList.split(",");
		for (String mobile : mobiles) {
			try {
				hermesServiceWrapper.sendMessage(mobile, content);
				if (type > 0) {
					hermesServiceWrapper.requestSendAudioMessage(mobile, content, -1);
				}
			} catch (Exception e) {
				logger.error("send alerm failed", e);
			}
		}
	}

	/**
	 * 通过手机号验证
	 * 
	 * @param code
	 * @param mobile
	 * @return success or not
	 * @throws UserException
	 */
	@Override
	public boolean validateCodeWithReceiver(String code, String mobile) throws UserException {
		try {
			logger.info("验证码验证, code:{}, mobile:{}", code, mobile);
			boolean pass = hermesServiceWrapper.validateCodeWithReceiver(code, mobile);
			logger.info("验证" + (pass ? "通过" : "失败") + ", code:{}, mobile:{}", code, mobile);
			return pass;
		} catch (UserException e) {
			logger.error("验证失败, code:{}, message:{}", e.getErrorCode(), e.getMessage());
			throw e;
		}
	}

	/**
	 * 给指定手机发送指定内容到短信
	 * 
	 * @param mobile
	 * @param content
	 * @return taskId
	 * @throws UserException
	 * @throws SystemException
	 */
	@Override
	public long sendMessage(String mobile, String content) throws UserException {
		try {
			logger.info("手机:{} ,发送短信:{}", mobile, content);
			long result = hermesServiceWrapper.sendMessage(mobile, content);
			logger.info("发送成功 taskid: {}, mobile: {}, content: {}", result, mobile, content);
			return result;
		} catch (UserException e) {
			logger.error("短信发送失败, 手机:{}, code:{}, message:{}", mobile, e.getErrorCode(), e.getMessage());
			throw e;
		}
	}

	/**
	 * 更新callTaskInfo状态
	 * 
	 * @param taskId
	 * @param errorLog
	 * @param newStatus
	 * @param updateTime
	 * @return
	 */

	@Override
	public void updateCallTaskInfo(long taskId, String errorLog, int newStatus, int updateTime) {
		try {
			if (!hermesServiceWrapper.isRedisCallTaskInfoExist(taskId)) {
				return;
			}
			logger.info("更新Redis callTaskInfo, taskId:{}, error_log:{}, newStatus:{}, updateTime :{}", taskId,
					errorLog, newStatus, updateTime);
			CallTaskInfo callTaskInfo = hermesServiceWrapper.getRedisCallTaskInfo(taskId);
			if (callTaskInfo != null) {
				callTaskInfo.setError_log(errorLog);
				callTaskInfo.setStatus(newStatus);
				callTaskInfo.setUpdate_time(updateTime);
				hermesServiceWrapper.updateRedisCallTaskInfo(callTaskInfo);
			}
			logger.info("更新Redis callTaskInfo成功, taskId:{}, error_log:{}, newStatus:{}, updateTime:{}", taskId,
					errorLog, newStatus, updateTime);
		} catch (SystemException e) {
			logger.error("更新redis callTaskInfo失败, taskId:{}, error_log:{}, newStatus:{}, updateTime :{}", taskId,
					errorLog, newStatus, updateTime);
		}
		try {
			CallTaskInfo callTaskInfo = hermesServiceWrapper.getDbCallTaskInfo(taskId);
			if (callTaskInfo == null) {
				return;
			}
			logger.info("更新DB callTaskInfo, taskId:{}, error_log:{}, newStatus:{}, updateTime :{}", taskId, errorLog,
					newStatus, updateTime);
			callTaskInfo.setError_log(errorLog);
			callTaskInfo.setStatus(newStatus);
			callTaskInfo.setUpdate_time(updateTime);
			hermesServiceWrapper.updateDbCallTaskInfo(callTaskInfo);
			logger.info("更新DB callTaskInfo成功, taskId:{}, error_log:{}, newStatus:{}, updateTime:{}", taskId, errorLog,
					newStatus, updateTime);
		} catch (SystemException e) {
			logger.error("更新DB callTaskInfo失败, taskId:{}, error_log:{}, newStatus:{}, updateTime :{}", taskId,
					errorLog, newStatus, updateTime);
		}
	}

	public void setHermesServiceWrapper(HermesServiceWrapper hermesServiceWrapper) {
		this.hermesServiceWrapper = hermesServiceWrapper;
	}

	public void setDevMobileList(String devMobileList) {
		this.devMobileList = devMobileList;
	}

	public String getDevMobileList() {
		return devMobileList;
	}

	public HermesServiceWrapper getHermesServiceWrapper() {
		return hermesServiceWrapper;
	}

	@Override
	public String getSmsSenderKey() {
		return this.hermesServiceWrapper.getSmsSenderKey();
	}

}
