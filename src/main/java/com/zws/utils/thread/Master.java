package com.zws.utils.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/**
 * @author zhengws
 * @create 2019/3/20 10:25
 */
@Slf4j
public class Master<T, E> {
    private ConcurrentLinkedQueue<T> workQueue = new ConcurrentLinkedQueue<T>();

    private ConcurrentHashMap<Integer, E> resultMap = new ConcurrentHashMap<>();

    private Worker<T, E> worker;

    private ExecutorService executorService;

    private int workerNumber;

    public Master(Worker<T, E> worker, ExecutorService executorService, int workerNumber) {
        this.worker = worker;
        this.executorService = executorService;
        worker.setWorkQueue(this.workQueue);
        worker.setResultMap(this.resultMap);
        this.workerNumber = workerNumber;
    }

    public void submit(T task) {
        this.workQueue.add(task);
    }

    public void execute() {
        worker.setCountDownLatch(new CountDownLatch(workQueue.size()));
        for (int i = 0; i < workerNumber; i++) {
            executorService.submit(worker);
        }
    }

    public ConcurrentHashMap<Integer, E> getResult() {
        try {
            worker.getCountDownLatch().await();
            log.info("Total task execute completed");
            return this.resultMap;
        } catch (InterruptedException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
