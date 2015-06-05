package me.ele.talaris.utils;

import java.lang.reflect.ParameterizedType;

public class Generics {

    /**
     * 这个函数是一个hack，用于判定E的真实类型。来自于
     * 
     * Code by Any Other Name | Reflecting generics | by Ian Robertson
     * 
     * 
     * 目前这个函数不是完美解决泛型的类型判断问题，匿名类还不能使用。
     * 
     * @return
     */
    public static Class<?> returnedClass(Object obj) {
        Class<? extends Object> clazz = obj.getClass();
        ParameterizedType parameterizedType = null;
        while (clazz != Object.class) {
            try {
                parameterizedType = (ParameterizedType) clazz.getGenericSuperclass();
            } catch (Throwable e) {
                clazz = clazz.getSuperclass();
                continue;
            }
            break;
        }

        return (Class<?>) parameterizedType.getActualTypeArguments()[0];
    }

}
