package me.ele.talaris.web.framework.compress;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

public class InitNeedCompressInterfaces implements BeanPostProcessor, Ordered {
    public static Set<String> needCompressInterfaces = new HashSet<String>();

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE + 2;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

	@Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        if (clazz.getAnnotation(Controller.class) != null) {
            // 若class上加上了部分uri
            RequestMapping requestMapping = (RequestMapping) clazz.getAnnotation(RequestMapping.class);
            String partUri = "";
            if (requestMapping != null) {
                partUri = requestMapping.value()[0];
            }
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                // 是接口，且加上了压缩注解
                if (method.getAnnotation(RequestMapping.class) != null) {
                    if (method.getAnnotation(Compress.class) != null) {
                        String uri = method.getAnnotation(RequestMapping.class).value()[0];
                        needCompressInterfaces.add(partUri + uri);
                    }
                }
            }
        }
        return bean;
    }
}
