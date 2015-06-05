package me.ele.talaris.web.log.report;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import me.ele.talaris.exception.SystemException;
import me.ele.talaris.model.Context;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author zhengwen 切面，用来记录日志。暂定存在内存中，定时任务写数据。
 * 
 *
 */
@Aspect
public class CountAspect {
    /**
     * 详细记录每个接口被谁调用了，也就是下面的队列
     */

    public static BlockingQueue<InterfaceLog> log_Queue = new ArrayBlockingQueue<InterfaceLog>(10000);
    private final static Logger logger = LoggerFactory.getLogger(CountAspect.class);

    @Pointcut("@annotation(me.ele.talaris.web.log.report.InterfaceMonitor)")
    public void recordLog() {
    }

    @Around(value = "recordLog()")
    public Object processTx(ProceedingJoinPoint jp) throws java.lang.Throwable {
        // log_Queue.add(interfaceLog); 如果大于size则会抛出异常
        Signature signature = jp.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        InterfaceMonitor interfaceMonitor = method.getAnnotation(InterfaceMonitor.class);
        // 接口名称
        String interfaceName = interfaceMonitor.interfaceName();
        int contextIndex = interfaceMonitor.contextIndex();
        InterfaceLog interfaceLog = null;
        if (contextIndex == -1) {
            // 说明该接口调用的时候不需要context
            interfaceLog = new InterfaceLog(0, "", interfaceName, new Timestamp(System.currentTimeMillis()));
        } else {
            validateAnnotation(jp, interfaceMonitor);
            Context context = getContext(jp, interfaceMonitor);
            interfaceLog = new InterfaceLog(context.getUser().getId(), context.getUser().getName(), interfaceName,
                    new Timestamp(System.currentTimeMillis()));
        }
        try {
            log_Queue.add(interfaceLog);
        } catch (Exception e) {
            logger.info("日志记录队列满了");
        }
        return jp.proceed();

    }

    private Context getContext(ProceedingJoinPoint jp, InterfaceMonitor countUsed) {
        Object[] args = jp.getArgs();
        Context context = (Context) args[countUsed.contextIndex()];
        return context;
    }

    /**
     * 该方法只校验CountUsed注解上contextIndex不为默认值的场景
     * 
     * @param method
     * @param countUsed
     */
    private void validateAnnotation(ProceedingJoinPoint jp, InterfaceMonitor interfaceMonitor) {
        int contextIndex = interfaceMonitor.contextIndex();
        // 用户指定context位置超出方法参数个数
        Object[] args = jp.getArgs();
        if (args == null) {
            logger.debug("使用CountUsed参数指定位置超过方法参数个数");
            throw new SystemException("", "使用CountUsed参数指定位置超过方法参数个数");
        }
        if (args.length < contextIndex + 1) {
            logger.debug("使用CountUsed参数指定位置超过方法参数个数");
            throw new SystemException("", "使用CountUsed参数指定位置超过方法参数个数");
        }
        Object o = args[contextIndex];
        if (!(o instanceof Context)) {
            logger.debug("使用CountUsed指定参数不为Context类型");
            throw new SystemException("", "使用CountUsed指定参数不为Context类型");
        }
    }

}
