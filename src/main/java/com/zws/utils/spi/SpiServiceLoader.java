package com.zws.utils.spi;


import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author zhengws
 * @date 2020-12-31 21:01
 */
@Slf4j
public final class SpiServiceLoader {
    /**
     * 维护已注册服务
     */
    private final static Map<Class, Collection<Class<?>>> registeredService = new ConcurrentHashMap<>();

    /**
     * 获取 service 实例列表.
     *
     * @param service
     * @param <T>
     * @return
     */
    public static <T> Collection<T> newServiceInstances(Class<T> service) {
        return newServiceInstances(service, null, null);
    }

    /**
     * 获取 serveice实例，并初始化参数
     *
     * @param service
     * @param initMethod
     * @param params
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> Collection<T> newServiceInstances(Class<T> service, Method initMethod, Object... params) {
        Collection<Class<?>> classes = getAndAutoRegisterService(service);
        if (classes == null) return Collections.emptyList();

        Collection<T> instances = new LinkedList<>();
        T instance;
        for (Class<?> clazz : classes) {
            try {
                instance = (T) clazz.newInstance();
                initMethodInvoke(initMethod, params, instance);
                instances.add(instance);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return instances;
    }

    /**
     * 获取并自动注册
     *
     * @param service
     * @param <T>
     * @return
     */
    public static <T> Collection<Class<?>> getAndAutoRegisterService(Class<T> service) {
        Collection<Class<?>> classes = registeredService.get(service);
        if (classes == null) {
            register(service);
            classes = registeredService.get(service);
        }
        return classes;
    }


    private static <T> void initMethodInvoke(Method initMethod, Object[] params, T instance) throws Exception {
        if (initMethod != null && params != null) {
            boolean accessible = initMethod.isAccessible();
            try {
                initMethod.setAccessible(true);
                initMethod.invoke(instance, params);
            } finally {
                initMethod.setAccessible(accessible);
            }
        }
    }

    /**
     * 注册SIP接口
     *
     * @param service
     * @param <T>
     */
    private static <T> void register(final Class<T> service) {
        AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
            ServiceLoader<T> serviceLoader = ServiceLoader.load(service);
            Iterator<T> iterator = serviceLoader.iterator();
            T instance;
            while (iterator.hasNext()) {
                registerServiceClass(service, instance = iterator.next());
                log.info("{} has registered.", instance.getClass().getSimpleName());
            }
            return null;
        });
    }


    private static <T> void registerServiceClass(Class<T> service, T instance) {
        Collection<Class<?>> classes = registeredService.computeIfAbsent(service, k -> new LinkedList<>());
        classes.add(instance.getClass());
    }
}
