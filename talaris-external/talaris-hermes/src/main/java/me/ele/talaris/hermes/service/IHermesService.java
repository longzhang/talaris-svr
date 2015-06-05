package me.ele.talaris.hermes.service;

import me.ele.talaris.exception.SystemException;
import me.ele.talaris.exception.UserException;
import me.ele.talaris.hermes.model.SendVerifyCodeResult;

public interface IHermesService {

	/**
	 * 给指定手机发送一个语音验证码
	 * 
	 * @param mobile
	 * @return 包含发送的验证码和hash_value
	 * @throws UserException
	 */
	public SendVerifyCodeResult requestSendVerifyCodeUseVoice(String mobile)
			throws UserException;

	/**
	 * 给指定手机发送短信验证码
	 * 
	 * @param mobile
	 * @return 包含发送的验证码和hash_value
	 * @throws UserException
	 */
	public SendVerifyCodeResult requestSendVerifyCode(String mobile)
			throws UserException;

	/**
	 * 给指定手机发送语音短信
	 * 
	 * @param mobile
	 * @param content
	 * @return taskId
	 * @throws UserException
	 */

	public long sendAudioMessage(String mobile, String content,
			long deliveryOrderId) throws UserException;

	/**
	 * 配送中短信通知
	 * 
	 * @param recMobile
	 * @param courierMobile
	 * @param name
	 * @return
	 * @throws UserException
	 */

	public long sendDeliveringMessage(String recMobile, String courierMobile,
			String name) throws UserException;
	
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
			String name, String url) throws UserException;

	/**
	 * 送达短信通知
	 * 
	 * @param recMobile
	 * @param courierMobile
	 * @param name
	 * @return
	 * @throws UserException
	 */

	public long sendDeliveredMessage(String recMobile, String courierMobile,
			String name) throws UserException;

	/**
	 * 餐厅绑定通知短信
	 * 
	 * @param recMobile
	 * @param loginMobile
	 * @param resName
	 * @return
	 * @throws UserException
	 */

	public long sendRestaurantBindNotification(String recMobile,
			String loginMobile, String resName) throws UserException;

	/**
	 * 发送App下载链接
	 * 
	 * @param mobile
	 * @param name
	 * @param url
	 * @return taskId
	 * @throws SystemException
	 */

	public long sendAppDownloadUrlMessage(String mobile, String name, String url)
			throws UserException;

	/**
	 * 异常报警
	 * 
	 * @param content
	 * @param type
	 *            大于0表示严重警报，发送语音通知，小于0只发送短信
	 */

	public void safeSendAlarm(String content, int type);

	/**
	 * 通过手机号验证
	 * 
	 * @param code
	 * @param mobile
	 * @return success or not
	 * @throws UserException
	 */
	public boolean validateCodeWithReceiver(String code, String mobile)
			throws UserException;

	/**
	 * 给指定手机发送指定内容到短信
	 * 
	 * @param mobile
	 * @param content
	 * @return taskId
	 * @throws UserException
	 * @throws SystemException
	 */
	public long sendMessage(String mobile, String content) throws UserException;

	/**
	 * 更新callTaskInfo状态
	 * 
	 * @param taskId
	 * @param errorLog
	 * @param newStatus
	 * @param updateTime
	 * @return
	 */

	public void updateCallTaskInfo(long taskId, String errorLog, int newStatus,
			int updateTime);

	public String getSmsSenderKey();


}