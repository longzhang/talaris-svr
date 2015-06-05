/**
 * 
 */
package me.ele.talaris.exception;

/**
 * @author dawson
 * @date 2015年5月14日
 * @version v1.0
 * @description
 */
public class NoticeException extends Exception {
	/**
	 * 在序列化时如果类名相同，serialVersionUID也相同，那么就认为是同一个类
	 */
	private static final long serialVersionUID = 1L;
	protected String errorCode;

	public NoticeException(String errorCode, String errorMsg, Throwable cause) {
		super(errorMsg, cause);
		this.errorCode = errorCode;
	}

	public NoticeException(String errorCode, String errorMsg) {
		super(errorMsg);
		this.errorCode = errorCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
}
