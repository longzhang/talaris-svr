/**
 * 
 */
package me.ele.talaris.service.auth.impl;

import java.util.Map;

import me.ele.talaris.dao.UserDeviceDao;
import me.ele.talaris.exception.ExceptionCode;
import me.ele.talaris.exception.ExceptionFactory;
import me.ele.talaris.exception.SystemException;
import me.ele.talaris.exception.UserException;
import me.ele.talaris.model.SystemParameter;
import me.ele.talaris.redis.RedisClient;
import me.ele.talaris.response.VersionInfo;
import me.ele.talaris.service.auth.IUserDeviceService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author shaorongfei
 */
@Service
public class UserDeviceService implements IUserDeviceService {
	private final static Logger logger = LoggerFactory.getLogger(LoginService.class);
	@Autowired
	private UserDeviceDao userDeviceDao;

	@Autowired
	private RedisClient redisClient;

	@Override
	public boolean updateUserDeviceByToken(String token) throws UserException, SystemException {
		boolean result = false;
		try {
			result = (userDeviceDao.logoutUpdateByToken(token) > 0 ? true : false);
		} catch (Exception e) {
			logger.error("通过token更新userDevice失败,token:{},exception:{}", token, e);
			throw ExceptionFactory.newSystemException(ExceptionCode.APP_ERROR_552);
		}
		return result;
	}
	
	/**
	 * 更新用户登录状态
	 * @param userId
	 * @return
	 */
	@Override
	public int updateTokenByUserId(int userId){
		return userDeviceDao.updateUserDeviceIsValidByUserId(userId);
	}

	/**
	 * 获取version版本信息
	 * 
	 * @param appVersion
	 * @return
	 * @throws UserException
	 * @throws SystemException
	 */
	@Override
	public VersionInfo getAppVersionInfo(String appVersion) throws UserException, SystemException {

		VersionInfo versionInfo = null;
		try {
//			boolean isKeyExist = redisClient.isKeyExist(RedisKeySet.APP_VERSION_KEY);
//			if (isKeyExist) {
//				logger.info("RedisKey{},appVersion{}", RedisKeySet.APP_VERSION_KEY, appVersion);
//				return redisClient.getAnObject(RedisKeySet.APP_VERSION_KEY, VersionInfo.class);
//			}

			Map<String, SystemParameter> paramsMap = InternalSystemParameterService.getInstance().getAllParamsMap();
			SystemParameter isForce = paramsMap.get(SystemParameter.CODE_SYSTEM_TALARIS_APK_ISFORCE);
			SystemParameter deviceVersion = paramsMap.get(SystemParameter.CODE_SYSTEM_TALARIS_APK_LATEST_VERSION);
			SystemParameter update_content = paramsMap.get(SystemParameter.CODE_SYSTEM_TALARIS_APK_UPDATE_CONTENT);
			SystemParameter url = paramsMap.get(SystemParameter.CODE_SYSTEM_TALARIS_APK_LATEST_VERSION_URL);
			versionInfo = new VersionInfo();
			if (isForce != null) {
				versionInfo.setIs_force(Integer.valueOf(isForce.getValue()));
			}
			if (deviceVersion != null) {
				versionInfo.setVersion(deviceVersion.getValue());
			}
			if (update_content != null) {
				versionInfo.setUpdate_content(update_content.getValue());
			}
			if (url != null) {
				versionInfo.setUrl(url.getValue());
			}
			// 不一致即设置为新版本
			if (!appVersion.equals(deviceVersion.getValue())) {
				versionInfo.setHas_new(1);
			}
//			if (StringUtils.isNotBlank(versionInfo.getVersion())) {
//				redisClient.setAnObject(RedisKeySet.APP_VERSION_KEY, versionInfo);
//			}
		} catch (Exception e) {
			throw ExceptionFactory.newSystemException(ExceptionCode.BASE_SYSTEM_PARAMETER_ERR_03.getCode(), "获取系统配置异常");
		}
		return versionInfo;
	}
}
