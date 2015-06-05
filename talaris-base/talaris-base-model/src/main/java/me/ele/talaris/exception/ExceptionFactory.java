package me.ele.talaris.exception;

public class ExceptionFactory {
	public static UserException newUserException(IExceptionCode codeEnum, Throwable e) {
		String code = codeEnum.getCode();
		String msg = codeEnum.getMessage();
		return new UserException(code, msg, e);
	}

	public static UserException newUserException(IExceptionCode codeEnum) {
		String code = codeEnum.getCode();
		String msg = codeEnum.getMessage();
		return new UserException(code, msg);
	}

	public static UserException newUserException(String code, String msg, Throwable e) {
		return new UserException(code, msg, e);
	}

	public static UserException newUserException(String code, String msg) {
		return new UserException(code, msg);
	}

	public static SystemException newSystemException(IExceptionCode codeEnum, Throwable e) {
		String code = codeEnum.getCode();
		String msg = codeEnum.getMessage();
		return new SystemException(code, msg, e);
	}

	public static SystemException newSystemException(IExceptionCode codeEnum) {
		String code = codeEnum.getCode();
		String msg = codeEnum.getMessage();
		return new SystemException(code, msg);
	}

	public static SystemException newSystemException(String code, String msg, Throwable e) {
		return new SystemException(code, msg, e);
	}

	public static SystemException newSystemException(String code, String msg) {
		return new SystemException(code, msg);
	}

	public static SystemException newSystemException(String msg, Exception e) {
		return new SystemException(msg, e);

	}
}
