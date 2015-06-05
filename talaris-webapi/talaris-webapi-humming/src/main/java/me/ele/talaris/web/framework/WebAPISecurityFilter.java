package me.ele.talaris.web.framework;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ele.talaris.model.Context;
import me.ele.talaris.model.User;
import me.ele.talaris.model.UserStationRole;
import me.ele.talaris.service.user.IUserService;
import me.ele.talaris.service.user.IUserStationRoleService;
import me.ele.talaris.utils.SerializeUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

public class WebAPISecurityFilter implements Filter {

    final static Logger logger = LoggerFactory.getLogger(WebAPISecurityFilter.class);
    final static Logger iplogger = LoggerFactory.getLogger("ip");
    @Autowired
    IUserService userService;

    @Autowired
    IUserStationRoleService userStationRoleService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        corsResponse(httpResponse);
        String sourceIp = getIpAddr(httpRequest);
        if (sourceIp != null) {
            iplogger.info("源IP:{}", sourceIp);
        }
        // 忽略FS下载LINK
        if (httpRequest.getRequestURI().indexOf(WebAPISecurityProtocol.FS_FILE) > 0) {
            chain.doFilter(request, response);
            return;
        }

        // 忽略登录验证链接
        if (httpRequest.getRequestURI().indexOf(WebAPISecurityProtocol.AUTH_URI) > 0) {
            chain.doFilter(request, response);
            return;
        }

        // 版本更新
        if (httpRequest.getRequestURI().indexOf(WebAPISecurityProtocol.APP_VERSION) > 0) {
            chain.doFilter(request, response);
            return;
        }

        if (httpRequest.getRequestURI().indexOf(WebAPISecurityProtocol.SEND_CODE) > 0) {
            chain.doFilter(request, response);
            return;
        }

        if (httpRequest.getRequestURI().indexOf(WebAPISecurityProtocol.SEND_VOICE_CODE) > 0) {
            chain.doFilter(request, response);
            return;
        }

        if (httpRequest.getRequestURI().indexOf(WebAPISecurityProtocol.CALL_BACK_STATUS) > 0) {
            chain.doFilter(request, response);
            return;
        }

        if (httpRequest.getRequestURI().indexOf(WebAPISecurityProtocol.WALLE_POST) > 0) {
            chain.doFilter(request, response);
            return;
        }

        if (httpRequest.getRequestURI().indexOf(WebAPISecurityProtocol.ZABBIX_MONITOR) > 0) {
            chain.doFilter(request, response);
            return;
        }
        if (httpRequest.getRequestURI().indexOf(WebAPISecurityProtocol.APP_DOWNLOAD) > 0) {
            chain.doFilter(request, response);
            return;
        }

        ResponseEntity<?> error = null;
        try {
            error = securityCheck(httpRequest, httpResponse);
        } catch (Exception e) {
            logger.error("security check error", e);
            error = new ResponseEntity<String>("ERR-UNKNOWN", "未知错误", e.getMessage());
        }
        if (error != null) {
            httpResponse.setContentType("application/json");
            httpResponse.setCharacterEncoding("UTF-8");
            httpResponse.getWriter().write(SerializeUtil.beanToJson(error));
            httpResponse.getWriter().flush();
            return;
        }

        chain.doFilter(request, response);
    }

    private ResponseEntity<?> securityCheck(HttpServletRequest request, HttpServletResponse response) {
        logger.info(request.toString());
        logger.info(request.getHeader(WebAPISecurityProtocol.HTTP_ACCESS_TOKEN));
        User user = userService.getUserByToken(request.getHeader(WebAPISecurityProtocol.HTTP_ACCESS_TOKEN));
        if (user == null) {
            logger.debug("security Check error : " + WebAPIExceptions.ERR_AUTH_FAILED);
            return new ResponseEntity<String>(WebAPIExceptions.ERR_AUTH_FAILED, "无效身份", "");
        }

        List<UserStationRole> userStationRoles = userStationRoleService.listUserStationRoleByUserId(user.getId());
        if (CollectionUtils.isEmpty(userStationRoles)) {
            return new ResponseEntity<String>(WebAPIExceptions.ERR_AUTH_FAILED, "无效身份", "");
        }
        Context context = new Context(user, userStationRoles);
        request.setAttribute("context", context);

        if (!WebAPISecurityProtocol.checkSign(request)) {
            return new ResponseEntity<String>(WebAPIExceptions.ERR_BAD_REQUEST, "无效请求", "");
        }

        return null;
    }

    /**
     * Cross-origin resource sharing (CORS)
     * 
     * @param response
     */
    private void corsResponse(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", WebAPISecurityProtocol.CUSTOM_HEADERS);
    }

    public void destroy() {
    }

    public String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

}
