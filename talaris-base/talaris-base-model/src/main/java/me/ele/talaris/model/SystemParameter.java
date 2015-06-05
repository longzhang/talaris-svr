/**
 * 
 */
package me.ele.talaris.model;

import me.ele.talaris.bean.Human;
import me.ele.talaris.framework.dao.Column;
import me.ele.talaris.framework.dao.Table;

/**
 * @author chaoguodeng
 *
 */
@Table(name = "sys_system_parameter")
@Human(label = "系统配置")
public class SystemParameter {
	public static final String CODE_SYSTEM_TALARIS_APK_LATEST_VERSION = "SYSTEM_TALARIS_APK_LATEST_VERSION"; // talaris的安卓和IOS最新版本
	public static final String CODE_SYSTEM_TALARIS_APK_LATEST_VERSION_URL = "SYSTEM_TALARIS_APK_LATEST_VERSION_URL";// talaris的安卓和IOS最新版本url
	public static final String CODE_SYSTEM_TALARIS_APK_ISFORCE = "SYSTEM_TALARIS_APK_ISFORCE";// talaris的安卓和IOS是否强制升级
	public static final String CODE_SYSTEM_TALARIS_APK_UPDATE_CONTENT = "SYSTEM_TALARIS_APK_UPDATE_CONTENT";// talaris的安卓和IOS更新内容
	public static final String CODE_SYSTEM_TALARIS_APK_LICENCE = "SYSTEM_TALARIS_APK_LICENCE";//用户同意协议内容
	public static final String CODE_SYSTEM_TALARIS_APK_EXPIRETIME = "SYSTEM_TALARIS_APK_EXPIRETIME";//
	public static final String isForce = "1";
	
	@Column(pk = true, auto_increase = true)
	@Human(label = "系统配置id")
	private int id;

	@Column
	@Human(label = "系统配置代码")
	private String code;

	@Column
	@Human(label = "系统配置值")
	private String value;

	@Column
	@Human(label = "系统配置名称")
	private String name;

	@Column
	@Human(label = "系统配置描述")
	private String description;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public SystemParameter(int id, String code, String value, String name,
			String description) {
		this.id = id;
		this.code = code;
		this.value = value;
		this.name = name;
		this.description = description;
	}

	public SystemParameter() {
		super();
	}

}
