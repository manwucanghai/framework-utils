package com.zws.utils.tools;

import com.zws.utils.exception.AppException;
import lombok.extern.slf4j.Slf4j;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhengws
 * @date 2021-03-14
 */
@Slf4j
public class MapUtils {

    @SuppressWarnings("unchecked")
    public static Map<String, Object> objectToMap(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Map) {
            return (Map<String, Object>) value;
        }
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(value.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            Map<String, Object> results = new HashMap<>();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                if (key.compareToIgnoreCase("class") == 0) {
                    continue;
                }
                Method method = property.getReadMethod();
                if (method != null && Modifier.isPublic(method.getModifiers())) {
                    Object val = method.invoke(value);
                    results.put(key, val);
                } else {
                    log.warn("{} get method is not public.", property.getName());
                }
            }
            return results;
        } catch (Exception e) {
            throw new AppException(e);
        }
    }


    public static <T> T mapToObject(Map<String, ?> map, Class<T> beanClass) throws Exception {
        if (map == null) {
            return null;
        }

        T obj = beanClass.newInstance();

        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            Method method = property.getWriteMethod();
            if (method != null && Modifier.isPublic(method.getModifiers())) {
                method.invoke(obj, map.get(property.getName()));
            } else {
                log.warn("{} set method is not public.", property.getName());
            }
        }
        return obj;
    }
}
