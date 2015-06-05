package me.ele.talaris.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.BeanUtils;

/**
 * 拷贝对象类，因为很多时候会拷贝一个list的对象，需要写for循环，所以在这里写了个util类
 * 
 * @author zhengwen
 *
 */
public class BeanCopyUtil {
    /**
     * 将一个list的对象拷贝转化为另外一个类型的list
     * 
     * @param ks 原list
     * @param v 需要转化为的对象类型
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    @SuppressWarnings("unchecked")
    public static <K, V> List<V> transform(List<K> ks, Class<V> vClass) throws InstantiationException, IllegalAccessException {
        if (ks == null || ks.size() == 0)
        {
            return Collections.emptyList();
        }
        List<V> vs = new ArrayList<>();
        for (K k : ks) {
            V v2 = vClass.newInstance();
            BeanUtils.copyProperties(k, v2);
            vs.add(v2);
        }
        return vs;
    }

    /**
     * 将一个list的对象拷贝转化为另外一个类型的list
     * @param ks 原list
     * @param v 需要转化为的对象类型
     * @param ignoreFields  忽略的属性
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */

    @SuppressWarnings("unchecked")
    public static <K, V> List<V> transform(List<K> ks, Class<V> vClass, String... ignoreFields) throws IllegalAccessException, InstantiationException {
        if (ks == null || ks.size() == 0)
        {
            return Collections.emptyList();
        }
        List<V> vs = new ArrayList<>();
        for (K k : ks) {
            V v2 = vClass.newInstance();
            BeanUtils.copyProperties(k, v2, ignoreFields);
            vs.add(v2);
        }
        return vs;
    }

    /**
     *
     * @param ks
     * @param vClass
     * @param <K>
     * @param <V>
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static <K, V> V transform(K ks, Class<V>vClass) throws IllegalAccessException, InstantiationException {
        V vs = vClass.newInstance();
        BeanUtils.copyProperties(ks, vs);

        return vs;
    }


    /**
     *
     * @param ks
     * @param vClass
     * @param ignoreFields
     * @param <K>
     * @param <V>
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static <K, V> V transform(K ks, Class<V> vClass, String... ignoreFields) throws IllegalAccessException, InstantiationException {
        V vs = vClass.newInstance();
        BeanUtils.copyProperties(ks, vs, ignoreFields);
        return vs;
    }

}
