package me.ele.talaris.exception;

import java.util.List;

public class SystemException extends RuntimeException {

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    private static final long serialVersionUID = 1L;
    protected String errorCode;
    private List<String> ids;

    public SystemException(String errorCode, String errorMsg, Throwable cause) {
        super(errorMsg, cause);
        this.errorCode = errorCode;
    }

    public SystemException(String errorCode, String errorMsg, List<String> ids) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.ids = ids;
    }

    public SystemException(String errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
    }

    public SystemException(String errorMsg, Throwable cause) {
        super(errorMsg, cause);
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
