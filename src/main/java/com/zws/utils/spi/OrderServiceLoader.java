package com.zws.utils.spi;


import com.zws.utils.order.OrderAware;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

/**
 * @author zhengws
 * @date 2020-09-12 14:18
 */
public class OrderServiceLoader {

    private OrderServiceLoader() {
    }

    /**
     * 获取已注册服务列表，并按规则排序
     *
     * @param service
     * @param <T>
     * @return
     */
    public static <T extends OrderAware> Collection<T> getRegisteredInstances(Class<T> service) {
        return getRegisteredInstances(service, null, null, null);
    }

    /**
     * 获取已注册服务列表，并排序且按一定规则过滤
     *
     * @param service
     * @param filter
     * @param <T>
     * @return
     */
    public static <T extends OrderAware> Collection<T> getRegisteredInstances(Class<T> service, Function<T, Boolean> filter) {
        return getRegisteredInstances(service, null, null, filter);
    }

    /**
     * 获取已注册服务列表，并排序且按一定规则过滤, 指定初始化方法
     *
     * @param service
     * @param initMethod
     * @param params
     * @param filter
     * @param <T>
     * @return
     */
    public static <T extends OrderAware> Collection<T> getRegisteredInstances(
            Class<T> service, Method initMethod, Object params, Function<T, Boolean> filter) {

        Map<Integer, T> result = new TreeMap<>();
        for (T instance : SpiServiceLoader.newServiceInstances(service, initMethod, params)) {
            //apply 结果为true时，添加。
            if (filter != null && !filter.apply(instance)) {
                continue;
            }
            result.put(instance.getOrder(), instance);
        }
        return result.values();
    }
}
