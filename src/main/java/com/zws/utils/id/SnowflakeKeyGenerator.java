package com.zws.utils.id;

import com.zws.utils.tools.DateUtils;

/**
 * 雪花ID生成器
 * 中心编号支持(0-3)
 * 机器编号支持(0-15)
 * 年限支持：2110年以前【小程序端，最大支持17位数字，超过失真】.
 *
 * @author zhengws
 * @date 2020-09-07 19:33
 */
public class SnowflakeKeyGenerator implements IKeyGenerator {
    /**
     * 开始时间截（2020-01-01 00:00:00.000）
     */
    private static final long offsetTime = 1577808000000L;

    /**
     * 中心编码占2个bits
     */
    private static final long dataCenterIdBits = 2L;

    /**
     * 最大中心数(0-3)
     */
    private static final long dataCenterIdMask = ~(-1L << dataCenterIdBits);

    /**
     * 机器编码占4个bits
     */
    private static final long workerIdBits = 4L;

    /**
     * 最大机器数(0-16)
     */
    private static final long workerIdMask = ~(-1L << workerIdBits);

    /**
     * 相同时间内，序列号编码占9bits.
     */
    private static final long sequenceBits = 9L;

    /**
     * 机器ID向左移9位
     */
    private static final long workerIdShift = sequenceBits;

    /**
     * 中心标识id向左移13位(9+4)
     */
    private static final long dataCenterIdShift = sequenceBits + workerIdBits;

    /**
     * 时间截向左移15位(9+4+2)
     */
    private static final long timestampLeftShift = sequenceBits + workerIdBits + dataCenterIdBits;

    /**
     * 生成序列的掩码，这里为512
     */
    private static final long sequenceMask = ~(-1L << sequenceBits);

    /**
     * 数据中心ID(0~15)
     */
    private long dataCenterId;

    /**
     * 工作机器ID(0~15)
     */
    private long workerId;

    /**
     * 毫秒内序列(0~511)
     */
    private long sequence = 0L;

    /**
     * 上次生成ID的时间截
     */
    private long lastTimestamp = -1L;

    /**
     * @param dataCenterId 数据中心ID (0~3)
     * @param workerId     工作ID (0~15)
     */
    public SnowflakeKeyGenerator(long dataCenterId, long workerId) {
        if (dataCenterId > dataCenterIdMask || dataCenterId < 0) {
            throw new IllegalArgumentException("dataCenterId can't be greater than " + dataCenterIdMask + " or less than 0");
        }
        if (workerId > workerIdMask || workerId < 0) {
            throw new IllegalArgumentException("workerId can't be greater than " + workerIdMask + " or less than 0");
        }
        this.dataCenterId = dataCenterId;
        this.workerId = workerId;
    }


    public static long parseDataCenterId(long id) {
        return dataCenterIdMask & (id >> dataCenterIdShift);
    }

    public static long parseWorkerId(long id) {
        return workerIdMask & (id >> workerIdShift);
    }

    public static long parseSequence(long id) {
        return sequenceMask & id;
    }

    public static long parseLastTimestamp(long id) {
        return (id >> timestampLeftShift) + offsetTime;
    }

    @Override
    public long nextKey() {
        long timestamp = now();
        //系统时钟回退, 抛出异常.
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("Clock back. offset time %d milliseconds", lastTimestamp - timestamp));
        }

        //高并发时间相同，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                //毫秒内序列溢出, 自旋, 获得新的时间戳
                timestamp = nextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }
        lastTimestamp = timestamp;

        return ((timestamp - offsetTime) << timestampLeftShift)
                | (dataCenterId << dataCenterIdShift)
                | (workerId << workerIdShift)
                | sequence;
    }

    @Override
    public String nextStrKey() {
        return Long.toString(nextKey());
    }

    @Override
    public long nextKey(Object type) {
        return nextKey();
    }

    @Override
    public String nextStrKey(Object type) {
        return nextStrKey();
    }

    /**
     * 自旋，获取下一个时间戳
     */
    private long nextMillis(long lastTimestamp) {
        long timestamp = now();
        while (timestamp <= lastTimestamp) {
            timestamp = now();
        }
        return timestamp;
    }

    /**
     * 获取当前时间戳
     */
    private long now() {
        return DateUtils.getNow();
    }
}
