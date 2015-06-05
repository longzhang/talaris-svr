package me.ele.talaris.webapi.auth;

import java.sql.Timestamp;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import me.ele.talaris.base.dto.HotUpdateInfo;
import me.ele.talaris.base.service.IHotUpdateService;
import me.ele.talaris.exception.SystemException;
import me.ele.talaris.exception.UserException;
import me.ele.talaris.model.Context;
import me.ele.talaris.model.User;
import me.ele.talaris.response.LicenceResult;
import me.ele.talaris.response.LoginInfo;
import me.ele.talaris.response.VersionInfo;
import me.ele.talaris.service.auth.ILoginService;
import me.ele.talaris.service.auth.IUserDeviceService;
import me.ele.talaris.service.auth.IVerificationCodeService;
import me.ele.talaris.utils.SerializeUtil;
import me.ele.talaris.utils.Utils;
import me.ele.talaris.web.framework.ResponseEntity;
import me.ele.talaris.web.framework.WebAPIBaseController;
import me.ele.talaris.web.log.report.InterfaceMonitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author shaorongfei
 */
@Controller
@RequestMapping("/webapi")
public class AuthController extends WebAPIBaseController {
	private final static Logger logger = LoggerFactory.getLogger(AuthController.class);

	@Resource(name = "loginService")
	private ILoginService loginService;

	@Resource(name = "verificationCodeService")
	private IVerificationCodeService verificationCodeService;

	@Resource(name = "userDeviceService")
	private IUserDeviceService userDeviceService;

    @Autowired
    private IHotUpdateService hotUpdateService;

	/**
	 * 发送语音验证码
	 * 
	 * @param context1
	 * @param mobile
	 * @return
	 * @throws UserException
	 * @throws SystemException
	 */
	@InterfaceMonitor(interfaceName = "发送语音验证码")
	@RequestMapping(value = "/auth/send_voice_validate_code/", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<Integer> sendVoiceValidateCode(
			@RequestHeader("HTTP-DEVICE-TYPE") Integer deviceType, @RequestHeader("HTTP-DEVICE-ID") String deviceID,
			@RequestParam("mobile") String mobile) throws UserException, SystemException {
		if (!isMobile(mobile)) {
			return new ResponseEntity<Integer>("AUTH_ERROR_500", " 手机号不合法", null);
		}
		loginService.sendVoiceValidateCode(mobile, deviceType, deviceID);
		logger.info("发送语音验证码成功");
		return ResponseEntity.success(1);
	}

	/**
	 * 配送员发送验证码/老板发送验证码
	 * 
	 * @param context
	 * @param mobile
	 * @return
	 * @throws UserException
	 * @throws SystemException
	 */
	@InterfaceMonitor(interfaceName = "发送验证码")
	@RequestMapping(value = "/auth/send_validate_code/", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody ResponseEntity<Integer> sendValidateCode(
			@RequestHeader("HTTP-DEVICE-TYPE") Integer deviceType, @RequestHeader("HTTP-DEVICE-ID") String deviceID,
			@RequestParam("mobile") String mobile) throws UserException, SystemException {
		if (!isMobile(mobile)) {
			return new ResponseEntity<Integer>("AUTH_ERROR_400", "手机号不合法", null);
		}
		loginService.sendValidateCode(mobile, deviceType, deviceID);
		return ResponseEntity.success(1);
	}

	/**
	 * 验证并登录
	 * 
	 * @param context
	 * @param mobile
	 * @param validate_code
	 * @return
	 * @throws UserException
	 * @throws SystemException
	 */
	@InterfaceMonitor(interfaceName = "验证并登录")
	@RequestMapping(value = "/auth/login/", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody ResponseEntity<LoginInfo> login(@RequestHeader("HTTP-DEVICE-TYPE") Integer deviceType,
			@RequestHeader("HTTP-DEVICE-ID") String deviceID,
			@RequestHeader(value = "HTTP-APP-VERSION", required = false, defaultValue = "") String appVersion,
			@RequestParam("mobile") String mobile, @RequestParam("validate_code") String validate_code)
			throws UserException, SystemException {
		if (!isMobile(mobile)) {
			return new ResponseEntity<LoginInfo>("AUTH_ERROR_400", "手机号不合法", null);
		}
		LoginInfo loginInfo = loginService.login(mobile, validate_code, deviceType, deviceID, appVersion);
		logger.debug(SerializeUtil.beanToJson(loginInfo));
		logger.info("验证并登录成功");
		return ResponseEntity.success(loginInfo);
	}

	/**
	 * 获得个人信息
	 * 
	 * @param context
	 * @param mobile
	 * @param validate_code
	 * @return
	 * @throws UserException
	 * @throws SystemException
	 */
	@InterfaceMonitor(interfaceName = "获得个人信息", contextIndex = 0)
	@RequestMapping(value = "/my_info/", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseEntity<Object> getMyInfo1(Context context) throws UserException, SystemException {
		ResponseEntity<Object> rsp = ResponseEntity.success(new Object());
		if (context.getUser() != null) {
			rsp.setData(context.getUser());
		} else {
			rsp.setErr_code("USER_ERROR_530");
			rsp.setMsg("您没有权限或者系统异常");
		}
		return rsp;

	}

	/**
	 * 补全个人信息
	 * 
	 * @param context
	 * @param mobile
	 * @param validate_code
	 * @return
	 * @throws UserException
	 * @throws SystemException
	 */
	@InterfaceMonitor(interfaceName = "补全个人信息", contextIndex = 0)
	@RequestMapping(value = "/my_info/", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody ResponseEntity<User> updateMyInfo(Context context,
			@RequestHeader("HTTP-ACCESS-TOKEN") String encoding, @RequestParam("user_name") String user_name)
			throws UserException, SystemException {
		if(!Utils.isChineseCharacter(user_name)){
			return new ResponseEntity<User>("AUTH_ERROR_401", "系统异常", null);
		}
		User user = context.getUser();
		user.setName(user_name);
		// user.setCertificate_number(certificate_number);
		user.setUpdated_at(new Timestamp(new Date().getTime()));
		loginService.updateUserInfo(context, user);
		logger.info("补全个人信息成功,name:{},mobile:{}", user.getName(), user.getMobile());
		return ResponseEntity.success(user);
	}

	/**
	 * 查看版本信息 在登陆前即调用，app里面也调用
	 * 
	 * @param context
	 * @return
	 * @throws UserException
	 * @throws SystemException
	 */
	@InterfaceMonitor(interfaceName = "获取版本接口")
	@RequestMapping(value = "/app/version/", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseEntity<VersionInfo> getAppVersion(Context context,
			@RequestHeader("HTTP-APP-VERSION") String appVersion) throws UserException, SystemException {
		// "url": '',//更新url
		// “has_new”: 1,//是否有新版本 1，0
		// "is_force": "1",//是否强制升级 1，0
		// "version": "1.1.1",//version
		// "update_content": "新的内容"
		VersionInfo versionInfo = userDeviceService.getAppVersionInfo(appVersion);
		logger.debug(SerializeUtil.beanToJson(versionInfo));
		logger.info("获取版本接口成功");
		return ResponseEntity.success(versionInfo);
	}

    /**
     * 查看h5热更新版本信息
     *
     * @param context
     * @return
     * @throws UserException
     * @throws SystemException
     */
    @InterfaceMonitor(interfaceName = "获取版本接口")
    @RequestMapping(value = "/app/version/hotupdate", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody ResponseEntity<HotUpdateInfo> getAppHotUpdateVersion(Context context) throws UserException, SystemException {
        // "url": '',//更新url
        // “has_new”: 1,//是否有新版本 1，0
        // "is_force": "1",//是否强制升级 1，0
        // "version": "1.1.1",//version
        // "update_content": "新的内容"
        HotUpdateInfo updateInfo = hotUpdateService.getLatestHotUpdateInfo();

        return ResponseEntity.success(updateInfo);
    }

	/**
	 * 更新SystemParameter，如果是
	 * 
	 * @param context
	 * @param appVersion
	 * @return
	 * @throws UserException
	 * @throws SystemException
	 */
	@RequestMapping(value = "/app/systemParameter/update/", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody ResponseEntity<Boolean> updateSystemParameter(Context context,
			@RequestParam("code") String code, @RequestParam("value") String value,
			@RequestHeader("HTTP-ACCESS-TOKEN") String encoding) throws UserException, SystemException {
		boolean updateResult = loginService.updateSystemParameter(context, code, value);
		if (updateResult) {
			logger.info("更新系统配置成功");
		}
		return ResponseEntity.success(updateResult);

	}

	/**
	 * 登出接口
	 * 
	 * @param encoding
	 * @return
	 * @throws UserException
	 * @throws SystemException
	 */
	@InterfaceMonitor(interfaceName = "登出接口")
	@RequestMapping(value = "/auth/logout", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody ResponseEntity<String> logout(@RequestHeader("HTTP-ACCESS-TOKEN") String encoding)
			throws UserException, SystemException {
		// 通过token来更新
		userDeviceService.updateUserDeviceByToken(encoding);
		logger.info("登出接口成功");
		return ResponseEntity.success("");
	}

	/**
	 * 获取用户协议接口
	 * 
	 * @param encoding
	 * @return
	 * @throws UserException
	 * @throws SystemException
	 */
	@InterfaceMonitor(interfaceName = "获取用户协议接口")
	@RequestMapping(value = "/auth/get_userlicence", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseEntity<LicenceResult> getUserLicence(
			@RequestHeader("HTTP-ACCESS-TOKEN") String encoding) throws UserException, SystemException {
		// 通过token来更新
		return ResponseEntity.success(loginService.getLicense());
	}

	/**
	 * 更改用户协议状态接口
	 * 
	 * @param encoding
	 * @return
	 * @throws UserException
	 * @throws SystemException
	 */
	@InterfaceMonitor(interfaceName = "更改用户协议状态接口")
	@RequestMapping(value = "/auth/update_user_licence_status", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody ResponseEntity<String> updateUserLicenceStatus(
			@RequestHeader("HTTP-ACCESS-TOKEN") String encoding) throws UserException, SystemException {
		loginService.agreeLicense(encoding);
		logger.info("更改用户协议状态接口成功");
		return ResponseEntity.success("200");
	}

	@RequestMapping(value = "/auth/test/", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseEntity<Integer> sendValidateCode(Context context) throws UserException,
			SystemException {
		logger.info("测试成功");
		return ResponseEntity.success(1);
	}

	/**
	 * 国家号码段分配如下 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188 　　
	 * 联通：130、131、132、152、155、156、185、186、176
	 *
	 * 电信：133、153、180、189、（1349卫通） 粗略验证，和前端保持一致
	 * 
	 * @param str
	 * @return
	 */
	private boolean isMobile(String str) {
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		// /^1[3|4|5|7|8]\d{9}$/
		// ^[1]([3][0-9]{1}|59|58|88|89|76)[0-9]{8}$
		p = Pattern.compile("^1[3|4|5|7|8]\\d{9}$"); // 验证手机号,
		m = p.matcher(str);
		b = m.matches();
		return b;
	}


	// public static void main(String args[]) {
	// String hanzi = "我是中国人";
	// System.out.println(isChineseCharacter(hanzi));
	// }
}
