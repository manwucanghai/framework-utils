package com.zws.utils.cache;


import com.zws.utils.thread.factory.AppThreadFactory;
import com.zws.utils.tools.DateUtils;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author zhengws
 * @time 2020/6/11
 * @description
 **/
public class ExpireCache<K, V> implements IExpireCache<K, V> {
    private long expireTimeMs;
    private long bulkTimeMs;
    private Node<K> headNode, lastNode;
    private Map<K, CacheValue<K, V>> resultMap = new WeakHashMap<>();
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private Lock readLock = readWriteLock.readLock();
    private Lock writeLock = readWriteLock.writeLock();
    private ScheduledExecutorService schedule;
    private static ThreadFactory threadFactory;

    public ExpireCache(int expireTime, TimeUnit unit) {
        this(expireTime, unit, 10);
    }

    public ExpireCache(int expireTime, TimeUnit unit, int bulkSize) {
        this(expireTime, unit, bulkSize, null);
    }

    public ExpireCache(int expireTime, TimeUnit unit, ScheduledExecutorService schedule) {
        this(expireTime, unit, 10, schedule);
    }

    public ExpireCache(int expireTime, TimeUnit unit, int bulkSize, ScheduledExecutorService schedule) {
        if (expireTime <= 0) {
            throw new RuntimeException("expireTime can't be low zero.");
        }
        this.schedule = schedule;
        this.expireTimeMs = unit.toMillis(expireTime);
        if (bulkSize <= 0) bulkSize = 10;
        this.bulkTimeMs = this.expireTimeMs / bulkSize;
        startSchedule();
    }

    private void startSchedule() {
        if (schedule == null) {
            if (threadFactory == null) {
                synchronized (ExpireCache.class) {
                    if (threadFactory == null) {
                        threadFactory = AppThreadFactory.getInstance("expire-cache-schedule");
                    }
                }
            }
            schedule = Executors.newScheduledThreadPool(1, threadFactory);
        }

        schedule.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                evict();
            }
        }, expireTimeMs, bulkTimeMs, TimeUnit.MILLISECONDS);
    }

    /**
     * 自动过期
     */
    private void evict() {
        final Lock writeLock = this.writeLock;
        try {
            writeLock.lock();
            while (headNode != null && headNode.isExpire()) {
                for (K key : headNode.keys) {
                    resultMap.remove(key);
                }
                headNode = headNode.next;
            }
        } finally {
            writeLock.unlock();
        }

    }

    @Override
    public V put(K key, V value) {
        final Lock writeLock = this.writeLock;
        try {
            writeLock.lock();
            Node<K> node = getLastNode(key);
            CacheValue<K, V> cacheValue = resultMap.computeIfAbsent(key, k -> new CacheValue<>(value, node));
            cacheValue.value = value;
            checkUpdateNode(key, node, cacheValue);
        } finally {
            writeLock.unlock();
        }
        return null;
    }

    /**
     * 检查并更新节点
     *
     * @param key
     * @param node
     * @param cacheValue
     */
    private void checkUpdateNode(K key, Node<K> node, CacheValue<K, V> cacheValue) {
        if (cacheValue.node != node) {
            cacheValue.node.keys.remove(key);
            cacheValue.node = node;
        }
    }

    /**
     * 获取当前最后一个节点
     *
     * @param key
     * @return
     */
    private Node<K> getLastNode(K key) {
        if (headNode == null) {
            headNode = lastNode = new Node<>(key);
        } else {
            if (lastNode.isInCurInterval()) {
                lastNode.addKey(key);
            } else {
                lastNode = lastNode.next = new Node<>(key);
            }
        }
        return lastNode;
    }

    @Override
    public V get(K key) {
        final Lock readLock = this.readLock;
        try {
            readLock.lock();
            CacheValue<K, V> cacheValue = resultMap.get(key);
            if (cacheValue != null) {
                checkUpdateNode(key, getLastNode(key), cacheValue);
                return cacheValue.value;
            }
        } finally {
            readLock.unlock();
        }
        return null;
    }

    @Override
    public Set<K> keySet() {
        final Lock readLock = this.readLock;
        try {
            readLock.lock();
            return resultMap.keySet();
        } finally {
            readLock.unlock();
        }
    }


    private class Node<K> {
        private long timestamp;
        private Set<K> keys = new LinkedHashSet<>();
        private Node<K> next;

        private Node(K key) {
            this.timestamp = DateUtils.getNow();
            addKey(key);
        }

        private void addKey(K key) {
            keys.add(key);
        }

        private boolean isExpire() {
            return DateUtils.getNow() - timestamp > expireTimeMs;
        }

        private boolean isInCurInterval() {
            return DateUtils.getNow() - timestamp < bulkTimeMs;
        }
    }

    private class CacheValue<K, V> {
        private V value;
        private Node<K> node;

        private CacheValue(V value, Node<K> node) {
            this.value = value;
            this.node = node;
        }
    }
}
