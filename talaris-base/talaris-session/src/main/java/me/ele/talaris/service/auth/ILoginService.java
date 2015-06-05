package me.ele.talaris.service.auth;

import me.ele.talaris.exception.SystemException;
import me.ele.talaris.exception.UserException;
import me.ele.talaris.model.Context;
import me.ele.talaris.model.User;
import me.ele.talaris.response.LicenceResult;
import me.ele.talaris.response.LoginInfo;


public interface ILoginService {

	/**
	 * 获取语音验证码
	 * 
	 * @param mobile
	 * @return
	 */
	public boolean sendVoiceValidateCode(String mobile, Integer deviceType,
			String deviceID) throws SystemException, UserException;

	/**
	 * 获取验证码
	 * 
	 * @param mobile
	 * @return
	 */
	public boolean sendValidateCode(String mobile, Integer deviceType,
			String deviceID) throws SystemException, UserException;

	/**
	 * * 验证并登录 通过deviceType判断登录流程，0，1，2->app流程,3->管理员登录流程
	 * 
	 * @param mobile
	 * @param validate_code
	 * @param deviceType
	 * @param deviceID
	 * @return
	 * @throws SystemException
	 * @throws UserException
	 */
	public LoginInfo login(String mobile, String validate_code,
			Integer deviceType, String deviceID, String appVersion)
			throws SystemException, UserException;

	/**
	 * 获取License声明
	 * 
	 * @return
	 */
	public LicenceResult getLicense();

	/**
	 * 补全个人信息
	 * 
	 * @param user
	 * @return
	 * @throws SystemException
	 * @throws UserException
	 */
	public int updateUserInfo(Context context, User user)
			throws SystemException, UserException;

	/**
	 * 更改同意协议接口 记录一条同意协议数据
	 * 
	 * @return
	 */
	public int agreeLicense(String token);

	/**
	 * 更新SystemParameter
	 * 
	 * @param code
	 * @param value
	 * @return
	 */
	public boolean updateSystemParameter(Context context, String code,
			String value) throws SystemException, UserException;

}