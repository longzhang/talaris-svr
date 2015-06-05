package me.ele.talaris.exception;

import java.util.List;

public class UserException extends Exception {
    /**
     * 在序列化时如果类名相同，serialVersionUID也相同，那么就认为是同一个类
     */
    private static final long serialVersionUID = 1L;
    protected String errorCode;

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    private List<String> ids;

    public UserException(String errorCode, String errorMsg, Throwable cause) {
        super(errorMsg, cause);
        this.errorCode = errorCode;
    }

    public UserException(String errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
    }

    public UserException(String errorCode, String errorMsg, List<String> ids) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.ids = ids;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
