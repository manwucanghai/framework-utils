package com.zws.utils.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhengws
 * @create 2019/3/20 10:27
 */
@Slf4j
public abstract class Worker<T, E> implements Runnable {
    private ConcurrentLinkedQueue<T> workQueue;
    private ConcurrentHashMap<Integer, E> resultMap;
    private CountDownLatch countDownLatch;
    private AtomicInteger taskIndex = new AtomicInteger();

    void setWorkQueue(ConcurrentLinkedQueue<T> workQueue) {
        this.workQueue = workQueue;
    }

    void setResultMap(ConcurrentHashMap<Integer, E> resultMap) {
        this.resultMap = resultMap;
    }

    void setCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    CountDownLatch getCountDownLatch() {
        return this.countDownLatch;
    }

    @Override
    public void run() {
        while (true) {
            T input = this.workQueue.poll();
            if (input == null) {
                break;
            }
            try {
                E output = handle(input);
                resultMap.put(taskIndex.getAndIncrement(), output);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            } finally {
                countDownLatch.countDown();
            }
        }
    }

    abstract E handle(T input);
}
