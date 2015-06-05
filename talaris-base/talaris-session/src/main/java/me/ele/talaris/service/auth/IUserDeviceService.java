package me.ele.talaris.service.auth;

import me.ele.talaris.exception.SystemException;
import me.ele.talaris.exception.UserException;
import me.ele.talaris.response.VersionInfo;

public interface IUserDeviceService {

	public boolean updateUserDeviceByToken(String token) throws UserException,
			SystemException;

	/**
	 * 更新用户登录状态
	 * @param userId
	 * @return
	 */
	public int updateTokenByUserId(int userId);

	/**
	 * 获取version版本信息
	 * 
	 * @param appVersion
	 * @return
	 * @throws UserException
	 * @throws SystemException
	 */
	public VersionInfo getAppVersionInfo(String appVersion) throws UserException,
			SystemException;

}