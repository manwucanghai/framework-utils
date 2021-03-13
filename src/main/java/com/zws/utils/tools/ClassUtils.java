package com.zws.utils.tools;


import com.zws.utils.exception.AppException;

import java.lang.reflect.Method;

/**
 * @author zhengws
 * @date 2020-09-19 13:49
 */
public class ClassUtils {
    /**
     * 获取特定方法的method
     *
     * @param target
     * @param methodName
     * @param methodParams
     * @return
     */
    public static Method getMethod(Class<?> target, String methodName, Class<?>... methodParams) {
        Method method = null;
        try {
            method = target.getMethod(methodName, methodParams);
        } catch (NoSuchMethodException e) {
            throw new AppException(e);
        }
        return method;
    }
}
