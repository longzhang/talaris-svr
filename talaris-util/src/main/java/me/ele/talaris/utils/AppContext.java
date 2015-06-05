/**
 * 
 */
package me.ele.talaris.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author shaorongfei
 *
 */
public enum AppContext {
	INSTANCE;

	public Properties configuration = new Properties();

	public void init() {
		InputStream is = this.getClass().getResourceAsStream("/mail-info.properties");
		if (is != null) {
			try {
				this.configuration.clear();
				this.configuration.load(is);
			} catch (IOException e) {
			} finally {
				try {
					is.close();
				} catch (Throwable t) {
				}
			}
		}
	}

	public String getConfigValue(String key) {
		return this.configuration.getProperty(key);
	}

}
