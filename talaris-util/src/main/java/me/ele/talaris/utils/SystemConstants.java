/**
 * 
 */
package me.ele.talaris.utils;

/**
 * @author shaorongfei
 *
 */
public class SystemConstants {
	// info
	public static final String MAILHOST = AppContext.INSTANCE.getConfigValue("mail.host");
	public static final String MAILPORT = AppContext.INSTANCE.getConfigValue("mail.port");
	public static final String MAILUSERNAME = AppContext.INSTANCE.getConfigValue("mail.username");
	public static final String MAILPASSWORD = AppContext.INSTANCE.getConfigValue("mail.password");
	public static final String MAILFROM = AppContext.INSTANCE.getConfigValue("mail.from");
	public static final String MAILTO = AppContext.INSTANCE.getConfigValue("mail.to");

}
