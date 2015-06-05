package me.ele.talaris.bean;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;



import me.ele.talaris.utils.Utils;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.module.SimpleModule;
import org.codehaus.jackson.type.TypeReference;

public class Beans {

    protected static class BigDecimalSerializer extends JsonSerializer<BigDecimal> {

        @Override
        public void serialize(BigDecimal value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
                JsonProcessingException {
            jgen.writeString(value.toPlainString());
        }

        @Override
        public Class<BigDecimal> handledType() {
            return BigDecimal.class;
        }

    }

    public static String toJSONString(Object obj) throws IOException {
        StringWriter writer = null;
        JsonGenerator gen = null;
        String json = null;
        try {
            try {
                ObjectMapper mapper = new ObjectMapper();
                SimpleModule module = new SimpleModule("bigdecimal", new Version(1, 0, 0, "first"));
                module.addSerializer(new BigDecimalSerializer());
                mapper.registerModule(module);
                writer = new StringWriter();
                gen = new JsonFactory().createJsonGenerator(writer);
                mapper.writeValue(gen, obj);
                json = writer.toString();
            } finally {
                if (gen != null) {
                    try {
                        gen.close();
                    } catch (IOException e) {
                    }
                }
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                    }
                }
            }
        } catch (IOException e) {
            throw e;
        }

        return json;
    }

    public static Object toJSONStringSafe(Object obj) {
        try {
            return toJSONString(obj);
        } catch (Throwable t) {
            return "";
        }
    }

    public static Object jsonToContainer(String json) throws JsonParseException, JsonMappingException, IOException {
        if (Utils.isEmptyWithTrim(json))
            return null;

        json = json.trim();

        ObjectMapper mapper = new ObjectMapper();

        if (json.charAt(0) == '[') {
            return mapper.readValue(json, new TypeReference<List<Object>>() {
            });

        } else if (json.charAt(0) == '{') {
            return mapper.readValue(json, new TypeReference<Map<String, Object>>() {
            });
        } else {
            throw new IllegalArgumentException("无法解析json字符串到一个container");
        }
    }

    /**
     * 这个函数将对象的一些属性抽取成一个Map，key是这个属性的
     * 
     * {@link Human.label }，如果没有label，则是这个属性的名字；value是这个属性的value，但如果这个属性定义了 {@link Human.getter }，则按照设置的getter获取value。
     * 
     * @param bean
     * @param properties
     * @return
     * @throws NoSuchFieldException
     * @throws SecurityException
     * @throws IntrospectionException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws NoSuchMethodException
     */
    public static Map<String, Object> toHumanMap(Object bean, String[] properties) throws SecurityException,
            NoSuchFieldException, IntrospectionException, IllegalArgumentException, IllegalAccessException,
            InvocationTargetException, NoSuchMethodException {
        return toMap(bean, properties, true);
    }

    public static List<String> listHumanVisibleProperties(Class<?> beanClass) throws SecurityException,
            NoSuchFieldException {
        List<String> filtered = new ArrayList<String>();
        List<String> properties = extractNonstaticProperties(beanClass);
        for (String propName : properties) {
            Field f = getDeclaredFieldRecursive(beanClass, propName);
            if (f.isAnnotationPresent(Human.class)) {
                Human ha = f.getAnnotation(Human.class);
                if (ha.visible() == false) {
                    continue;
                } else {
                    filtered.add(propName);
                }
            }
        }
        return filtered;
    }

    /**
     * 这个函数将对象的一些属性抽取成一个Map，key是这个属性的
     * 
     * {@link Human.label }，如果没有label，则是这个属性的名字；value是这个属性的value，但如果这个属性定义了 {@link Human.getter }，则按照设置的getter获取value。
     * 
     * @throws NoSuchFieldException
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws IntrospectionException
     * 
     */
    public static Map<String, Object> toMap(Object bean, String[] properties, boolean human) throws SecurityException,
            NoSuchFieldException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException,
            InvocationTargetException, IntrospectionException {

        // LinkedHashMap是有序的Map，HashMap无序，所以使用LinkedHashMap
        Map<String, Object> map = new LinkedHashMap<String, Object>();

        for (String propName : properties) {
            Field f = getDeclaredFieldRecursive(bean.getClass(), propName);
            String label = propName;
            Object value = null;
            if (human && f.isAnnotationPresent(Human.class)) {
                Human ha = f.getAnnotation(Human.class);
                if (ha.visible() == false)
                    continue;
                if (!Utils.isEmptyWithTrim(ha.label())) {
                    label = ha.label();
                }
                if (!Utils.isEmptyWithTrim(ha.getter())) {
                    Method method = bean.getClass().getMethod(ha.getter());
                    value = method.invoke(bean);
                } else {
                    PropertyDescriptor pd = new PropertyDescriptor(propName, bean.getClass());
                    value = pd.getReadMethod().invoke(bean);
                }
            } else {
                PropertyDescriptor pd = new PropertyDescriptor(propName, bean.getClass());
                value = pd.getReadMethod().invoke(bean);
            }

            map.put(label, value);
        }
        return map;
    }

    public static Map<String, Object> toMap(Object bean) throws BeanAccessException {
        List<String> properties = extractNonstaticProperties(bean.getClass());
        try {
            return toMap(bean, properties.toArray(new String[properties.size()]));
        } catch (Throwable e) {
            throw new BeanAccessException(e);
        }
    }

    public static Map<String, Object> toMap(Object bean, String[] properties) throws SecurityException,
            IllegalArgumentException, NoSuchFieldException, NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, IntrospectionException {
        return toMap(bean, properties, false);
    }

    private static Field getDeclaredFieldRecursive(Class<? extends Object> clazz, String propName)
            throws SecurityException, NoSuchFieldException {
        Class<? extends Object> startClass = clazz;
        while (clazz != Object.class) {
            try {
                Field f = clazz.getDeclaredField(propName);
                if (f != null)
                    return f;
            } catch (Throwable e) {

            }
            clazz = clazz.getSuperclass();
        }

        // 如果找不到，故意抛异常
        return startClass.getDeclaredField(propName);
    }

    /**
     * 这个函数将对象的全部属性抽取成一个Map，key是这个属性的 {@link Human }，如果没有Human，则是这个属性的名字。
     * 
     * 
     * @param bean
     * @return
     * @throws SecurityException
     * @throws IllegalArgumentException
     * @throws NoSuchFieldException
     * @throws IntrospectionException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    public static Map<String, Object> toHumanMap(Object bean) throws SecurityException, IllegalArgumentException,
            NoSuchFieldException, IntrospectionException, IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {

        List<String> properties = extractNonstaticProperties(bean.getClass());
        return toHumanMap(bean, properties.toArray(new String[properties.size()]));
    }

    private static List<String> extractNonstaticProperties(Class<?> beanClass) {
        List<Field> fields = getFieldsUpTo(beanClass, Object.class);

        List<String> properties = new ArrayList<String>();
        for (Field field : fields) {
            if (!Modifier.isStatic(field.getModifiers())) {
                properties.add(field.getName());
            }
        }
        return properties;
    }

    public static List<Field> getFieldsUpTo(Class<?> startClass, Class<?> exclusiveParent) {

        List<Field> currentClassFields = Utils.toList(startClass.getDeclaredFields());

        Class<?> parentClass = startClass.getSuperclass();

        if (parentClass != null && (exclusiveParent == null || !(parentClass.equals(exclusiveParent)))) {
            List<Field> parentClassFields = (List<Field>) getFieldsUpTo(parentClass, exclusiveParent);
            currentClassFields.addAll(0, parentClassFields);
        }

        return currentClassFields;
    }

    public static String retrieveLabel(Object bean, String propName) throws SecurityException, NoSuchFieldException {

        return retrieveLabel(bean.getClass(), propName);
    }

    public static String retrieveLabel(Object bean, String propName, String defaultLabel) {

        try {
            return retrieveLabel(bean.getClass(), propName);
        } catch (Throwable e) {
            return defaultLabel;
        }
    }

    public static String retrieveLabel(Class<?> beanClass, String propName) throws SecurityException,
            NoSuchFieldException {

        Field f = getDeclaredFieldRecursive(beanClass, propName);

        if (f.isAnnotationPresent(Human.class)) {
            Human ha = f.getAnnotation(Human.class);
            if (!Utils.isEmptyWithTrim(ha.label())) {
                return ha.label();
            }
        }
        return propName;
    }

    public static String retrieveClassLabel(Class<?> beanClass) {
        if (beanClass.isAnnotationPresent(Human.class)) {
            Human anno = beanClass.getAnnotation(Human.class);
            if (!Utils.isEmptyWithTrim(anno.label())) {
                return anno.label();
            }
        }

        return beanClass.getName();
    }

    public static String retrieveHumanValue(Object bean, String propName) throws SecurityException,
            NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException,
            NoSuchMethodException, IntrospectionException {

        Field f = getDeclaredFieldRecursive(bean.getClass(), propName);

        if (f.isAnnotationPresent(Human.class)) {
            Human ha = f.getAnnotation(Human.class);

            if (!Utils.isEmptyWithTrim(ha.getter())) {
                Method method = bean.getClass().getMethod(ha.getter());
                return Utils.safeToString(method.invoke(bean));
            }
        }

        PropertyDescriptor pd = new PropertyDescriptor(propName, bean.getClass());
        return Utils.safeToString(pd.getReadMethod().invoke(bean));

    }

    public static List<String> detectNullFields(Object bean) throws IllegalArgumentException, IllegalAccessException {
        List<Field> fields = getFieldsUpTo(bean.getClass(), Object.class);

        List<String> nullFields = new ArrayList<String>();

        for (Field f : fields) {
            f.setAccessible(true);
            Object value = f.get(bean);
            if (value == null) {
                nullFields.add(f.getName());
            }
        }
        return nullFields;
    }

    public static String retrieveConstant(Object obj, String propName) throws IllegalArgumentException,
            SecurityException, IllegalAccessException, NoSuchFieldException {
        return Utils.safeToString(obj.getClass().getDeclaredField(propName).get(null));
    }

    public static void setFieldValue(Object obj, String fieldName, Object value) throws IntrospectionException,
            IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        PropertyDescriptor pd = new PropertyDescriptor(fieldName, obj.getClass());
        pd.getWriteMethod().invoke(obj, value);
    }

}
