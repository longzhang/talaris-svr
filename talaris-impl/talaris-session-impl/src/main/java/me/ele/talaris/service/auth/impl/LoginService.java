/**
 * 
 */
package me.ele.talaris.service.auth.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import me.ele.talaris.constant.Constant;
import me.ele.talaris.dao.LicenceDao;
import me.ele.talaris.dao.RestaurantDao;
import me.ele.talaris.dao.RoleDao;
import me.ele.talaris.dao.StationDao;
import me.ele.talaris.dao.UserDeviceDao;
import me.ele.talaris.dao.UserLicenceDao;
import me.ele.talaris.dao.UserStationRoleDao;
import me.ele.talaris.exception.ExceptionCode;
import me.ele.talaris.exception.ExceptionFactory;
import me.ele.talaris.exception.IExceptionCode;
import me.ele.talaris.exception.SystemException;
import me.ele.talaris.exception.UserException;
import me.ele.talaris.hermes.model.SendVerifyCodeResult;
import me.ele.talaris.hermes.service.IHermesService;
import me.ele.talaris.model.Context;
import me.ele.talaris.model.Licence;
import me.ele.talaris.model.Restaurant;
import me.ele.talaris.model.Role;
import me.ele.talaris.model.Station;
import me.ele.talaris.model.SystemParameter;
import me.ele.talaris.model.User;
import me.ele.talaris.model.UserDevice;
import me.ele.talaris.model.UserLicence;
import me.ele.talaris.model.UserStationRole;
import me.ele.talaris.redis.RedisClient;
import me.ele.talaris.redis.RedisKeySet;
import me.ele.talaris.response.LicenceResult;
import me.ele.talaris.response.LoginInfo;
import me.ele.talaris.service.auth.AuthConstant;
import me.ele.talaris.service.auth.ILoginService;
import me.ele.talaris.service.station.IStationService;
import me.ele.talaris.service.user.IUserService;
import me.ele.talaris.service.user.IUserStationRoleService;
import me.ele.talaris.utils.Times;
import me.ele.talaris.utils.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * @author shaorongfei
 */
@Service
@Transactional
public class LoginService implements ILoginService {

	private final static Logger logger = LoggerFactory.getLogger(LoginService.class);

	@Autowired
	private IUserService userService;

	@Autowired
	private IHermesService coffeeHermesService;

	@Autowired
	private IStationService stationService;

	@Autowired
	StationDao stationDao;

	@Autowired
	private UserDeviceDao userDeviceDao;

	@Autowired
	private RestaurantDao restaurantDao;

	@Autowired
	private UserStationRoleDao userStationRoleDao;

	@Autowired
	private IUserStationRoleService userStationRoleService;

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private LicenceDao licenceDao;

	@Autowired
	private UserLicenceDao userLicenceDao;

	@Autowired
	private RedisClient redisClient;

	/**
	 * 获取语音验证码
	 * 
	 * @param mobile
	 * @return
	 */
	@Override
	public boolean sendVoiceValidateCode(String mobile, Integer deviceType, String deviceID) throws SystemException,
			UserException {
		if (StringUtils.isEmpty(mobile)) {
			throw ExceptionFactory.newUserException("AUTH_ERROR_501", "您输入手机号码有误");// 您输入手机号码有误
		}
		// App和后台管理首先都通过手机号查询user表
		User user = null;
		try {
			user = userService.getUserByMobile(mobile);
		} catch (Exception e) {
			logger.error("userService，getUserByMobile异常{}：", e);
			throw ExceptionFactory.newSystemException(ExceptionCode.AUTH_ERROR_513.getCode(), "系统异常");
		}
		if (deviceType == 3 || deviceType == 0 || deviceType == 1 || deviceType == 2) {
			// App流程
			if (user != null) {
				SendVerifyCodeResult sendSuccess = coffeeHermesService.requestSendVerifyCodeUseVoice(mobile);
				logger.info("获取验证码成功 userId:{},userName:{},userMobile:{},验证码是:{}", user.getId(), user.getName(),
						user.getMobile(), sendSuccess.getCode());
				return true;
			}
			// else 非App流程，通过deviceType走系统管理员流程
			else {
				// 查询本地Restaurant系统
				Restaurant restaurant = restaurantDao.getRestaurantByMobile(Long.valueOf(mobile));
				if (restaurant == null) {
					throw ExceptionFactory.newUserException(ExceptionCode.AUTH_ERROR_500.getCode(), "您的手机号码未绑定到餐厅");// 您的手机号码未绑定到餐厅
				}
				// 是餐厅管理员，发送验证码
				SendVerifyCodeResult sendSuccess = coffeeHermesService.requestSendVerifyCodeUseVoice(mobile);
				logger.info("获取验证码成功 userMobile:{},验证码是:{}", mobile, sendSuccess.getCode());
				return true;
			}

		} else {
			throw new UserException(ExceptionCode.AUTH_ERROR_512.getCode(), "您不是配送员");
		}
	}

	/**
	 * 获取验证码
	 * 
	 * @param mobile
	 * @return
	 */
	@Override
	public boolean sendValidateCode(String mobile, Integer deviceType, String deviceID) throws SystemException,
			UserException {
		if (StringUtils.isEmpty(mobile)) {
			throw ExceptionFactory.newUserException(ExceptionCode.AUTH_ERROR_501);// 您输入手机号码有误
		}
		// 通过手机号查询user表,
		User user = null;
		try {
			user = userService.getUserByMobile(mobile);
		} catch (Exception e) {
			logger.error("通过手机号查询用户异常{}：", e);
			throw ExceptionFactory.newSystemException(ExceptionCode.AUTH_ERROR_513.getCode(), "系统异常");
		}
		if (deviceType == 3 || deviceType == 0 || deviceType == 1 || deviceType == 2) {
			// App流程
			if (user != null) {
				SendVerifyCodeResult sendSuccess = coffeeHermesService.requestSendVerifyCode(mobile);
				logger.info("获取验证码成功 userId:{},userName:{},userMobile:{},验证码是:{}", user.getId(), user.getName(),
						user.getMobile(), sendSuccess.getCode());
				return true;
			}
			// else 非App流程，通过deviceType走系统管理员流程
			else {
				// 查询本地Restaurant系统
				Restaurant restaurant = restaurantDao.getRestaurantByMobile(Long.valueOf(mobile));
				if (restaurant == null) {
					throw ExceptionFactory.newUserException(ExceptionCode.AUTH_ERROR_500.getCode(), "您的手机号码未绑定到餐厅");// 您的手机号码未绑定到餐厅
				}
				// 是餐厅管理员，发送验证码
				SendVerifyCodeResult sendSuccess = coffeeHermesService.requestSendVerifyCode(mobile);
				logger.info("获取验证码成功 userMobile:{},验证码是:{}", mobile, sendSuccess.getCode());
				return true;
			}

		} else {
			throw new UserException(ExceptionCode.AUTH_ERROR_512.getCode(), "您不是配送员");
		}
	}

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
	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public LoginInfo login(String mobile, String validate_code, Integer deviceType, String deviceID, String appVersion)
			throws SystemException, UserException {
		boolean isfirstLogin = false;
		LoginInfo loginInfo = null;
		boolean verificationSuccess = false;
		// 通过手机号查询user表
		User user = null;
		try {
			user = userService.getUserByMobile(mobile);
		} catch (Exception e) {
			logger.error("通过手机号查询用户异常{}：", e);
			throw ExceptionFactory.newSystemException(ExceptionCode.AUTH_ERROR_513.getCode(), "系统异常");
		}
		// App登录流程
		if (deviceType == 1 || deviceType == 2 || deviceType == 0) {
			if (user != null) {
				List<UserStationRole> userStationRoleList = userStationRoleService.listUserStationRoleByUserId(user
						.getId());
				if (userStationRoleList == null || userStationRoleList.size() == 0) {
					throw ExceptionFactory.newSystemException(ExceptionCode.AUTH_ERROR_514.getCode(), "权限异常");
				}
				boolean isDeliverMan = false;
				for (UserStationRole userStationRole : userStationRoleList) {
					logger.info("userId:{},roleId:{}", user.getId(), userStationRole.getRole_id());
					if (userStationRole.getRole_id() == 2) {
						isDeliverMan = true;
					}
				}
				// 管理员App也可以登陆,先把下面注释掉
				// if (!isDeliverMan) {
				// throw
				// ExceptionFactory.newSystemException(ExceptionCode.AUTH_ERROR_514.getCode(),
				// "您不是配送员，没有权限操作");
				// }
				// 校验验证码
				verificationSuccess = coffeeHermesService.validateCodeWithReceiver(validate_code, mobile);
				if (!verificationSuccess) {
					logger.error("校验验证码失败,userId:{},userName:{},userMobile:{}", user.getId(), user.getName(),
							user.getMobile());
					throw ExceptionFactory.newUserException(ExceptionCode.AUTH_ERROR_511.getCode(), "您的手机号或者验证码不正确");
				}
				// userDevice判读用户是否已经登陆isValid(1登录 0登出)和
				// 通过userId和deviceType去查询userDevice看是否首次登陆,expireTime不需要考虑,后面SecurityFilter判断过期app直接跳转登录
				UserDevice userDevice = null;
				try {
					userDevice = userDeviceDao.getUserDeviceByUserIdAndDeviceType(user.getId(), deviceType);
				} catch (Exception e) {
					logger.error("userDeviceDao，getUserDeviceByUserIdAndDeviceType异常：", e);
					throw ExceptionFactory.newSystemException(ExceptionCode.AUTH_ERROR_513.getCode(), "系统异常");
				}
				// 配送员首次登录或者不同设备首次登录只写userDervice表
				if (userDevice == null) {
					// 每次登陆生成token数据，异常方法内部已做处理
					isfirstLogin = true;
					firstLoginToWrite(user, mobile, null, deviceType, deviceID, appVersion);// 首次登陆
				} else {
					// App
					// 1.通过前面usrDevice是否为null能判断首次登陆或者不同设备(devictType不同)同一账号首次登陆
					// 2.非首次登陆顶替上次登陆 更新token和updateTime
					// 3.首次登陆情况下都要新生成一条userDevice记录
					//
					try {
						updateUserDevice(user, deviceType);
					} catch (Exception e) {
						logger.error("updateUserDevice，同一类型设备登录顶替上次登录异常：", e);
						throw ExceptionFactory.newSystemException(ExceptionCode.AUTH_ERROR_513.getCode(), "系统异常");
					}
				}

				loginInfo = assemblyLoginInfo(loginInfo, user, deviceType, deviceID, isfirstLogin);
				if (userDevice == null) {
					loginInfo.setIs_user_info_intact(0);
				}
				logger.info("登录成功 userId:{},userName:{},userMobile:{}", user.getId(), user.getName(), user.getMobile());
				return loginInfo;
			} else {
				// 配送系统表里没有，通过手机号查询本地Restaurant系统，restaurant不为null，代表是老板
				Restaurant restaurant = restaurantDao.getRestaurantByMobile(Long.valueOf(mobile));
				if (restaurant == null) {
					throw new UserException(ExceptionCode.AUTH_ERROR_511.getCode(), "您不是配送员");
				} else {
					isfirstLogin = true;// user==null,restaurant!=null,walle开通蜂鸟配送老板首次登录App
					User userFirst = firstLoginToWrite(user, mobile, restaurant, deviceType, deviceID, appVersion);
					// 组装登录数据
					loginInfo = assemblyLoginInfo(loginInfo, userFirst, deviceType, deviceID, isfirstLogin);
					logger.info("管理员首次登录成功 userId:{},userName:{},userMobile:{}", userFirst.getId(),
							userFirst.getName(), userFirst.getMobile());
					return loginInfo;
				}
			}
		}
		// 管理员登录流程
		if (deviceType == 3) {
			UserDevice userDevice = null;
			// user!=null，非首次登录
			if (user != null) {
				// 校验验证码
				verificationSuccess = coffeeHermesService.validateCodeWithReceiver(validate_code, mobile);
				if (!verificationSuccess) {
					logger.error("校验验证码失败,userId:{},userName:{},userMobile:{}", user.getId(), user.getName(),
							user.getMobile());
					throw ExceptionFactory.newUserException(ExceptionCode.AUTH_ERROR_511.getCode(), "您的手机号或者验证码不正确");
				}
				// userDevice判读用户是否已经登陆isValid=1 isValid(1登录 0登出)
				// userId和deviceType两个值都有效,expireTime不需要考虑,后面SecurityFilter判断过期后app会直接跳转至登录界面
				try {
					userDevice = userDeviceDao.getUserDeviceByUserIdAndDeviceType(user.getId(), deviceType);
				} catch (Exception e) {
					logger.error("userDeviceDao，getUserDeviceByUserIdAndDeviceType异常：", e);
					throw ExceptionFactory.newSystemException(ExceptionCode.AUTH_ERROR_513.getCode(), "系统异常");
				}
				// 配送员首次登录或者不同设备首次登录只写userDervice表
				if (userDevice == null) {
					// 每次登陆生成token数据，异常方法内部已做处理
					firstLoginToWrite(user, mobile, null, deviceType, deviceID, appVersion);// 首次登陆
				} else {
					// App
					// 1.通过前面usrDevice是否为null能判断首次登陆或者不同设备(devictType不同)同一账号首次登陆
					// 2.非首次登陆顶替上次登陆 更新token和updateTime
					// 3.首次登陆情况下都要新生成一条userDevice记录
					//
					try {
						updateUserDevice(user, deviceType);
					} catch (Exception e) {
						logger.error("updateUserDevice，同一类型设备登录顶替上次登录异常：", e);
						throw ExceptionFactory.newSystemException(ExceptionCode.AUTH_ERROR_513.getCode(), "系统异常");
					}
				}
				// 组装登录数据
				loginInfo = assemblyLoginInfo(loginInfo, user, deviceType, deviceID, isfirstLogin);
				logger.info("管理员登录成功 userId:{},userName:{},userMobile:{}", user.getId(), user.getName(),
						user.getMobile());
				return loginInfo;
			}
			// 配送系统表里没有，通过手机号查询本地Restaurant系统，restaurant不为null，代表是老板
			Restaurant restaurant = restaurantDao.getRestaurantByMobile(Long.valueOf(mobile));
			if (restaurant == null) {
				logger.info("登录失败！您不是餐厅管理员mobile:{}", mobile);
				throw ExceptionFactory.newUserException(ExceptionCode.AUTH_ERROR_511.getCode(), "您不是餐厅老板也不是管理员");
			}
			// 校验验证码
			verificationSuccess = coffeeHermesService.validateCodeWithReceiver(validate_code, mobile);
			if (!verificationSuccess) {
				logger.info("登录失败 验证码不正确 userMobile:{}", mobile);
				throw ExceptionFactory.newUserException(ExceptionCode.AUTH_ERROR_511.getCode(), "您的手机号或者验证码不正确");
			}

			// 首次登录成功,写表信息User,UserDevice,Restaurant,UserStationRole
			if (userDevice == null) {
				User userFirst = firstLoginToWrite(user, mobile, restaurant, deviceType, deviceID, appVersion);
				// 组装登录数据
				loginInfo = assemblyLoginInfo(loginInfo, userFirst, deviceType, deviceID, isfirstLogin);
				logger.info("管理员首次登录成功 userId:{},userName:{},userMobile:{}", userFirst.getId(), userFirst.getName(),
						userFirst.getMobile());
				return loginInfo;
			}
		}
		return loginInfo;
	}

	/**
	 * 同一类型设备登录顶替上次登录
	 * 
	 * @param user
	 * @param oldToken
	 * @throws UserException
	 */
	private void updateUserDevice(User user, int deviceType) throws UserException {
		String newToken = UUID.randomUUID().toString();
		Date date = new Date();
		Timestamp timeStamp = new Timestamp(date.getTime());
		Map<String, SystemParameter> paramsMap = InternalSystemParameterService.getInstance().getAllParamsMap();
		SystemParameter expireTimeParameter = paramsMap.get(SystemParameter.CODE_SYSTEM_TALARIS_APK_EXPIRETIME);
		try {
			Timestamp expireTime = Times.timestampPlusDays(new Integer(expireTimeParameter.getValue()));
			// 本身deviceType置为登陆状态
			userDeviceDao.updateTokenAndIsValid(newToken, user.getId(), deviceType, timeStamp, expireTime);
			// 其它deviceType置为非登陆状态
			userDeviceDao.updateOtherDeviceTypeIsValid(deviceType, user.getId(), timeStamp, expireTime);
			logger.info("更新user_device，timeStamp{},expireTime{}", timeStamp, expireTime);
		} catch (Exception e) {
			logger.error("updateTokenByOldToken,异常：", e);
			throw ExceptionFactory.newSystemException(ExceptionCode.AUTH_ERROR_513.getCode(), "系统异常");
		}

	}

	/**
	 * 首次登录写表信息 返回stationManagerRoleId,User
	 * 
	 * @param mobile
	 * @return
	 */
	private User firstLoginToWrite(User user, String mobile, Restaurant restaurant, Integer deviceType,
			String deviceID, String appVersion) {
		User userFirst = null;
		try {
			Map<String, SystemParameter> paramsMap = InternalSystemParameterService.getInstance().getAllParamsMap();
			SystemParameter expireTimeParameter = paramsMap.get(SystemParameter.CODE_SYSTEM_TALARIS_APK_EXPIRETIME);
			Date date = new Date();
			Timestamp timeStamp = new Timestamp(date.getTime());
			Timestamp expireTime = Times.timestampPlusDays(new Integer(expireTimeParameter.getValue()));
			if (restaurant == null && user != null) {// 配送员首次登录或者同一账号多次登录，user表有信息，只写userDevice表,UserStationRole表应由管理员添加配送员时写入
				String access_token = UUID.randomUUID().toString();
				UserDevice userDevice = new UserDevice(user.getId(), deviceID, deviceType, access_token, appVersion,
						AuthConstant.deviceIsValid, timeStamp, expireTime);
				userDeviceDao.addUserDevice(userDevice);
				return user;
			}

			User newUser = new User("", Long.parseLong(mobile), "", "", AuthConstant.statusValid, AuthConstant.online,
					new BigDecimal(100), new BigDecimal(100), timeStamp, timeStamp);
			userFirst = userService.addNewUser(newUser);

			// 写userDevice表
			String access_token = UUID.randomUUID().toString();
			UserDevice userDevice = new UserDevice(userFirst.getId(), deviceID, deviceType, access_token, appVersion,
					AuthConstant.deviceIsValid, timeStamp, expireTime);
			userDeviceDao.addUserDevice(userDevice);

			// 写Restaurant表
			// restaurantDao.insertRestaurantInfo(restaurant);

			// 写userStationRole表，stationId先填0,由下次自动创建站点时update，roleId读权限表取
			int stationManagerRoleId = roleDao.getRoleIdByRoleName(Constant.STATIONMANAGER);
			UserStationRole userStationRole = new UserStationRole(userFirst.getId(), 0, stationManagerRoleId,
					AuthConstant.statusValid, timeStamp, timeStamp);
			userStationRoleDao.addUserStationRole(userStationRole);

			// 管理员首次登录创建站点
			stationService.createStationByUser(userFirst);
		} catch (Exception e) {
			logger.error("firstLoginToWrite异常：", e);
			throw ExceptionFactory.newSystemException(ExceptionCode.AUTH_ERROR_513.getCode(), "系统异常");
		}
		return userFirst;
	}

	/**
	 * 组装登录后数据,user对象保证有值
	 * 
	 * @param context
	 * @param loginInfo
	 * @param user
	 */
	private LoginInfo assemblyLoginInfo(LoginInfo loginInfo, User user, Integer deviceType, String deviceID,
			boolean isfirstLogin) {

		try {
			List<Role> roleList = new ArrayList<Role>();
			List<Station> stationList = new ArrayList<Station>();
			// 通过userId查询userStationRole表
			List<UserStationRole> userStationRolelist = userStationRoleService
					.listUserStationRoleByUserId(user.getId());
			for (UserStationRole userStationRole : userStationRolelist) {
				Role role = roleDao.getRoleByRoleId(userStationRole.getRole_id());
				roleList.add(role);
				List<Station> list = stationDao.getStationListByMobile(user.getMobile());
				if (list != null && list.size() > 0) {
					stationList = list;
				}
			}
			UserDevice userDevice = userDeviceDao.getUserDeviceByUserIdAndDeviceType(user.getId(), deviceType);
			logger.info("userId{},deviceType{}", user.getId(), deviceType);
			if (userDevice == null) {
				logger.info("userDevice is null");
			}
			loginInfo = new LoginInfo();
			loginInfo.setAccess_token(userDevice.getAccess_token());
			Map<String, SystemParameter> paramsMap = InternalSystemParameterService.getInstance().getAllParamsMap();
			SystemParameter expireTimeParameter = paramsMap.get(SystemParameter.CODE_SYSTEM_TALARIS_APK_EXPIRETIME);
			loginInfo.setExpire_minutes(new Integer(expireTimeParameter.getValue()) * 24 * 60);
			// boolean isUserInfoCompleted =
			// userService.isUserBaseInfoCompleted(user.getId());
			// 个人信息是否完整，完整置为1 第一次登录强制更新 下次登录再次校验
			if (!isfirstLogin) {
				loginInfo.setIs_user_info_intact(AuthConstant.isUserInfoComplete);
			}
			// type＝1，以后可能扩展;status=2(0:无效;1:有效不可发布;2:有效可发布)
			// 以后有可能通过不同人员权限区分type
			Licence licence = licenceDao.getLicenceByType(1, 2);
			boolean isAgreed = userLicenceDao.isAgreedUserLicence(user.getId(), licence.getId());
			// 是否同意协议
			if (isAgreed) {
				loginInfo.setHas_unconfirmed_new_license(AuthConstant.isAgree);
			}
			loginInfo.setUser(user);
			loginInfo.setRoles(roleList);
			loginInfo.setStations(stationList);
		} catch (Exception e) {
			logger.error("assemblyLoginInfo，异常：", e);
			throw ExceptionFactory.newSystemException(ExceptionCode.AUTH_ERROR_513.getCode(), "系统异常");
		}
		return loginInfo;
	}

	/**
	 * 是否是第一次登录 暂时保留以后可能要到
	 * 
	 * @param mobile
	 * @param type
	 * @return
	 */
	boolean isFirstLogin(String mobile, String type) {
		return false;

	}

	/**
	 * 获取License声明
	 * 
	 * @return
	 */
	@Override
	public LicenceResult getLicense() {
		LicenceResult licenceResult = new LicenceResult();
		// type＝1，以后可能扩展;status=2(0:无效;1:有效不可发布;2:有效可发布)
		// 以后有可能通过不同人员权限区分type
		Licence licence = licenceDao.getLicenceByType(1, 2);
		licenceResult.setLicence_content(licence.getContent());
		licenceResult.setLicence_version(licence.getVersion());
		return licenceResult;// 默认

	}

	/**
	 * 补全个人信息
	 * 
	 * @param user
	 * @return
	 * @throws SystemException
	 * @throws UserException
	 */
	@Override
	public int updateUserInfo(Context context, User user) throws SystemException, UserException {

		// origin code
		// for (UserStationRole userStationRole : context.getUserStationRoles())
		// {
		// if
		// (userService.getUserByUserId(userStationRole.getUser_id()).equals(user.getName()))
		// {
		// throw
		// ExceptionFactory.newSystemException(ExceptionCode.AUTH_ERROR_516.getCode(),
		// "该餐厅已存在姓名为" + user.getName() + "的配送员，请重新填写");
		// }
		// }
		if (Utils.isEmptyWithTrim(user.getName())) {
			throw ExceptionFactory.newSystemException(ExceptionCode.AUTH_ERROR_517.getCode(), "未填写配送员，请填写");
		}
		// wen.zheng updae this module
		User afterUpdateUser = userService.updateCourier(context, user.getMobile(), user.getName(),
				user.getCertificate_number());
		if (afterUpdateUser != null) {
			return 1;
		}
		return 0;
	}

	/**
	 * 更改同意协议接口 记录一条同意协议数据
	 * 
	 * @return
	 */
	@Override
	public int agreeLicense(String token) {
		int result = 0;
		Date date = new Date();
		Timestamp timeStamp = new Timestamp(date.getTime());
		UserDevice userDevice = userDeviceDao.getUserDeviceByToken(token);
		// type＝1，以后可能扩展;status=2(0:无效;1:有效不可发布;2:有效可发布)
		// 以后有可能通过不同人员权限区分type
		Licence licence = licenceDao.getLicenceByType(1, 2);
		UserLicence usreLicence = new UserLicence(userDevice.getUser_id(), licence.getId(), timeStamp);
		result = userLicenceDao.addUserLicence(usreLicence);
		return result;
	}

	/**
	 * 更新SystemParameter
	 * 
	 * @param code
	 * @param value
	 * @return
	 */
	@Override
	public boolean updateSystemParameter(Context context, String code, String value) throws SystemException,
			UserException {
		boolean setSucess = false;
		try {
			SystemParameter systemParameter = InternalSystemParameterService.getInstance().updateSystemParameter(code,
					value);
			setSucess = redisClient.setAnObject(RedisKeySet.APP_VERSION_KEY, systemParameter);
			logger.info("RedisKey:{},code:{},value:{},setSucess:{}", RedisKeySet.APP_VERSION_KEY, code, value,
					setSucess);
		} catch (Exception e) {
			logger.error("更新系统配置异常" + e);
			throw ExceptionFactory.newSystemException(ExceptionCode.BASE_SYSTEM_PARAMETER_ERR_02.getCode(), "更新系统配置异常");
		}
		return setSucess;

	}

}
