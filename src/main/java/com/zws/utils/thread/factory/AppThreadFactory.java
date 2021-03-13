package com.zws.utils.thread.factory;


import com.zws.utils.exception.UnSuportExecption;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhengws
 * @date 2020-09-17 21:11
 */
public class AppThreadFactory implements ThreadFactory {
    private String prefix;
    private final static Map<String, ThreadFactory> factoryMap = new ConcurrentHashMap<>();
    private final static AtomicInteger number = new AtomicInteger(0);

    private AppThreadFactory(String prefix) {
        this.prefix = prefix;
    }

    public static ThreadFactory getInstance(String prefix) {
        return factoryMap.computeIfAbsent(prefix,
                k -> new AppThreadFactory(prefix));
    }

    @Override
    public Thread newThread(Runnable runnable) {
        if (runnable == null) {
            throw new UnSuportExecption("runnable can't be null");
        }
        Thread thread = new Thread(runnable, prefix + number.getAndIncrement());
        thread.setDaemon(true);
        return thread;
    }
}
