package me.ele.talaris.web.framework;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class WebAPISecurityProtocol {

    public static final String AUTH_URI = "/auth/login/";
    public static final String FS_FILE = "/fs/file/";

    public static final String SEND_CODE = "auth/send_validate_code";

    public static final String SEND_VOICE_CODE = "auth/send_voice_validate_code";

    public static final String HTTP_ACCESS_TOKEN = "HTTP-ACCESS-TOKEN";

    public static final String HTTP_TIMESTAMP = "HTTP-TIMESTAMP";

    public static final String HTTP_DEVICE_ID = "HTTP-DEVICE-ID";
    
    public static final String HTTP_DEVICE_TYPE = "HTTP-DEVICE-TYPE";

    public static final String HTTP_SIGNATURE = "HTTP-SIGNATURE";

    public static final String HTTP_CONSUMER_KEY = "HTTP-CONSUMER-KEY";

    public static final String CUSTOM_HEADERS = "Origin, X-Requested-With, Content-Type, Accept, HTTP-CONSUMER-KEY, HTTP-DEVICE-TYPE,HTTP-DEVICE-ID, HTTP-SIGNATURE, HTTP-ACCESS-TOKEN, HTTP-TIMESTAMP,HTTP-APP-VERSION,WALLE_AUTH_KEY";
    public static final String CALL_BACK_STATUS = "inter/hermes/push";
    public static final String WALLE_POST = "inter/walle";
    public static final String ZABBIX_MONITOR = "inter/ping";
    public static final String APP_DOWNLOAD = "download";
    public static final String APP_VERSION = "/app/version";

    public static boolean checkSign(HttpServletRequest request) {
        String uri = request.getRequestURI();

        String timestamp = request.getHeader(HTTP_TIMESTAMP);

        String access_token = request.getHeader(HTTP_ACCESS_TOKEN);

        String device_id = request.getHeader(HTTP_DEVICE_ID);

        String consumer_key = request.getHeader(HTTP_CONSUMER_KEY);

        String signature = request.getHeader(HTTP_SIGNATURE);

        Map params = request.getParameterMap();

        // TODO: calculate sign and check.

        return true;
    }

}
