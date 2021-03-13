package com.zws.utils.cache;

import java.util.Set;

/**
 * @author zhengws
 * @time 2020/6/11
 * @description
 **/
public interface IExpireCache<K, V> {
    V put(K key, V value);

    V get(K key);

    Set<K> keySet();
}
