package me.ele.talaris.errorlog.processor;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.sql.Timestamp;
import java.util.Enumeration;

import me.ele.talaris.redis.RedisClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 统一日志管理处理类，该类只处理必要的error信息，方便开发人员从一台redis上直接获取到哪台机器出错了，请注意这里的ip是ip6的
 * 
 * @author zhengwen
 *
 */
@Service
public class ErrorLogProcessor implements InitializingBean {
    @Autowired
    RedisClient redisClient;
    public static String ErrorList = "ERRORS";
    private InetAddress inetAddress;

    public static final Logger LOGGER = LoggerFactory.getLogger(ErrorLogProcessor.class);

    public void writeErrorInfo(String errorMsg) {
        if (inetAddress == null) {
            LOGGER.info("获取本机IP失败");
            return;
        }
        String host = inetAddress.getHostAddress();
        StringBuffer sb = new StringBuffer(host).append(":").append(new Timestamp(System.currentTimeMillis()))
                .append(":").append(errorMsg);
        try {
            redisClient.lpushForErrorMsg(ErrorList, sb.toString());
        } catch (Exception e) {
            LOGGER.error("日志记录失败", e);
            LOGGER.info("记录错误日志失败");
        }
    }

    public void writeErrorInfo(Throwable e) {
        if (inetAddress == null) {
            LOGGER.info("获取本机IP失败");
            return;
        }
        String host = inetAddress.getHostAddress();
        StackTraceElement[] stackTraceElements = e.getStackTrace();
        StringBuffer sb = new StringBuffer(host).append(":").append(new Timestamp(System.currentTimeMillis()))
                .append(":");
        for (StackTraceElement stackTraceElement : stackTraceElements) {
            sb.append(stackTraceElement.toString().trim());
        }
        try {
            redisClient.lpushForErrorMsg(ErrorList, sb.toString().substring(0, 1024));
        } catch (Exception e2) {
            LOGGER.error("日志记录失败", e2);
            LOGGER.info("记录错误日志失败");
        }
    }

    private InetAddress getAddress() {
        try {
            for (Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces(); interfaces
                    .hasMoreElements();) {
                NetworkInterface networkInterface = interfaces.nextElement();
                if (networkInterface.isLoopback() || networkInterface.isVirtual() || !networkInterface.isUp()) {
                    continue;
                }
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                if (addresses.hasMoreElements()) {
                    return addresses.nextElement();
                }
            }
        } catch (SocketException e) {
            LOGGER.info("Error when getting host ip address: <{}>.", e.getMessage());
        }
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.inetAddress = getAddress();
    }
}
