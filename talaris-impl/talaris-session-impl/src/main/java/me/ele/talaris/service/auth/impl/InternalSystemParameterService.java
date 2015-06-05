/**
 * 
 */
package me.ele.talaris.service.auth.impl;

import java.util.Map;

import me.ele.talaris.dao.SystemParameterDao;
import me.ele.talaris.exception.ExceptionCode;
import me.ele.talaris.exception.ExceptionFactory;
import me.ele.talaris.exception.SystemException;
import me.ele.talaris.model.SystemParameter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author shaorongfei
 *
 */
@Service
public class InternalSystemParameterService {
	public static Logger logger = LoggerFactory.getLogger(InternalSystemParameterService.class);
	@Autowired
	SystemParameterDao systemParameterDao;

	public String getParameter(String code) throws SystemException {
		SystemParameter sp = findSystemParameter(code);
		return sp.getValue();
	}

	public String getParameter(String code, String defaultValue) {
		try {
			return getParameter(code);
		} catch (SystemException e) {
			logger.error("找不到系统参数，code=" + code, e);
			return defaultValue;
		}
	}

	public int getParameterInt(String code) throws SystemException {
		SystemParameter sp = findSystemParameter(code);
		try {
			return Integer.parseInt(sp.getValue());
		} catch (NumberFormatException e) {
			String msg = String.format("系统参数格式错误，code=%s，value=%s", code, sp.getValue());
			logger.error(msg, e);
			throw ExceptionFactory.newSystemException(ExceptionCode.BASE_SYSTEM_PARAMETER_ERR);
		}
	}

	private SystemParameter findSystemParameter(String code) throws SystemException {
		SystemParameter sp = this.systemParameterDao.findSystemParameter(code);
		if (sp == null) {
			String msg = "找不到系统参数，code=" + code;
			logger.error(msg);
			throw ExceptionFactory.newSystemException(ExceptionCode.BASE_SYSTEM_PARAMETER_ERR);
		}
		return sp;
	}

	/**
	 * 更新配置信息
	 * 
	 * @param code
	 * @param value
	 * @return
	 */
	public SystemParameter updateSystemParameter(String code, String value) {
		SystemParameter sp = this.systemParameterDao.updateSystemParameter(code, value);
		if (sp == null) {
			String msg = "找不到系统参数，code=" + code;
			logger.error(msg);
			throw ExceptionFactory.newSystemException(ExceptionCode.BASE_SYSTEM_PARAMETER_ERR);
		}
		return sp;
	}

	public Map<String, SystemParameter> findSystemParameters(String... code) {
		return this.systemParameterDao.findSystemParameters(code);
	}

	public Map<String, SystemParameter> getAllParamsMap() {
		return this.systemParameterDao.listAllParams();
	}

	/**
	 * FindBugs检测出警告，这里解释： 这个单例的构建与通常不同，一般都是在getInstance函数里判断和构造的。
	 * 但这个单例的属性依赖Spring的反向注入。 所以，这个单例模式利用Spring的ApplicationContext初始化来完成。
	 */
	public InternalSystemParameterService() {
		instance = this;
	}

	private static InternalSystemParameterService instance;

	public static InternalSystemParameterService getInstance() {
		if (instance == null)
			throw new IllegalStateException("无法获得系统服务实例，请检查Spring的ApplicationContext是否加载异常，或者没有配置SystemService。");
		return instance;
	}

}
