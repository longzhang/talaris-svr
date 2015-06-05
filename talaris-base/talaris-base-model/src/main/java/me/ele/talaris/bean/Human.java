package me.ele.talaris.bean;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 这个注解用来方便描述model，给Bean和属性加上人性化的label，有利于上层服务人性化的展示model。
 * 
 * 如果注释加载类名上，只有label生效，其他项直接忽略。
 * 
 * 
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.FIELD })
public @interface Human {
    String label() default "";

    /**
     * 
     * @return
     */
    String getter() default "";

    boolean visible() default true;
}
