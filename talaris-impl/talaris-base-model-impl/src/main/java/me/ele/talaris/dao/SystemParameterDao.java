/**
 * 
 */
package me.ele.talaris.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.ele.talaris.framework.dao.BaseSpringDao;
import me.ele.talaris.model.SystemParameter;

import org.springframework.jdbc.core.BeanPropertyRowMapper;

/**
 * @author shaorongfei
 *
 */
public class SystemParameterDao extends BaseSpringDao<SystemParameter> {

	public SystemParameterDao() {
		super(new BeanPropertyRowMapper<SystemParameter>(SystemParameter.class));
	}

	public SystemParameter findSystemParameter(String code) {
		List<SystemParameter> list = this.select("where code=?", code);
		if (list.size() == 0) {
			return null;
		}
		return list.get(0);
	}

	/**
	 * 可变参数接口
	 * 
	 * @param codes
	 * @return key=>code value=>SystemParameter
	 */
	public Map<String, SystemParameter> findSystemParameters(Object... codes) {
		String where = "where code=?";
		for (int i = 1; i < codes.length; i++) {
			where += " or code=?";
		}
		List<SystemParameter> list = this.select(where, codes);
		Map<String, SystemParameter> parameterMap = new HashMap<String, SystemParameter>();
		for (SystemParameter parameter : list) {
			parameterMap.put(parameter.getCode(), parameter);
		}
		return parameterMap;
	}

	public Map<String, SystemParameter> listAllParams() {
		List<SystemParameter> paramList = this.select("order by id");
		Map<String, SystemParameter> paramMap = new HashMap<String, SystemParameter>();
		for (SystemParameter param : paramList) {
			paramMap.put(param.getCode(), param);
		}
		return paramMap;
	}

	/**
	 * 更改配置
	 * 
	 * @param code
	 * @return
	 */
	public SystemParameter updateSystemParameter(String code, String value) {
		SystemParameter systemParameter = this.selectOneOrNull("where code = ?", code);
		systemParameter.setValue(value);
		this.update(systemParameter);
		return systemParameter;
	}

}
