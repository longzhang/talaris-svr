package me.ele.talaris.web.framework;

public class WebAPIExceptions {

    @ExceptionCode("无效身份")
    public static final String ERR_AUTH_FAILED = "ERR_WEBAPI_1000001";
    @ExceptionCode("无效请求")
    public static final String ERR_BAD_REQUEST = "ERR_WEBAPI_1000002";

    @ExceptionCode("未知错误")
    public static final String ERR_UNKNOWN = "ERR_WEBAPI_1000003";

    @ExceptionCode("校验错误")
    public static final String ERR_WEBAPI_VALIDATION = "ERR-WEBAPI-100004";

}
