/**
 * 
 */
package me.ele.talaris.response;

/**
 * @author shaorongfei
 */
public class VersionInfo {
	// "url": 'sdsdsd',//更新url
	// “has_new”: 1,//是否有新版本 1，0
	// "is_force": "1",//是否强制升级 1，0
	// "version": "1.1.1",//version版本
	// "update_content": "新的内容"//更新的内容
	//
	private int has_new = 0;
	private int is_force = 0;
	private String version;
	private String update_content;

	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getHas_new() {
		return has_new;
	}

	public void setHas_new(int has_new) {
		this.has_new = has_new;
	}

	public int getIs_force() {
		return is_force;
	}

	public void setIs_force(int is_force) {
		this.is_force = is_force;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public VersionInfo(int has_new, int is_force, String version, String update_content, String url) {
		super();
		this.has_new = has_new;
		this.is_force = is_force;
		this.version = version;
		this.update_content = update_content;
		this.url = url;
	}

	public VersionInfo() {
		super();
	}

	public String getUpdate_content() {
		return update_content;
	}

	public void setUpdate_content(String update_content) {
		this.update_content = update_content;
	}
}
