package me.ele.talaris.web.log.report;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用来做接口统计，若需要统计哪个接口，则在上面加上该注解
 * 
 * @author zhengwen
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface InterfaceMonitor {
    public String interfaceName() default "";

    public int contextIndex() default -1;

}

// 使用实例 contextIndex不是必填
// @ConutUsed(interfaceName="登录接口",contextIndex=0)
// void doSomeThing(Context context)
