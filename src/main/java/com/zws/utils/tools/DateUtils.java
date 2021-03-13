package com.zws.utils.tools;


import com.zws.utils.exception.AppException;

/**
 * @author zhengws
 * @date 2020-09-12 16:32
 */
public class DateUtils {
    private static final long ONEDAY = 24 * 60 * 60 * 1000L;

    private static final long ONEHOUR = 60 * 60 * 1000L;

    private static final long ONEMINUTE = 60 * 1000L;

    /**
     * 获取当前时间
     *
     * @return
     */
    public static long getNow() {
        return System.currentTimeMillis();
    }

    /**
     * 获取n天前时间
     *
     * @param days
     * @return
     */
    public static long getDelayDays(int days) {
        if (days < 0) {
            throw new AppException("days can't be below zero");
        }
        return getNow() - days * ONEDAY;
    }

    /**
     * 获取n 分钟后时间点
     *
     * @param minute
     * @return
     */
    public static long getAfterMinutes(int minute) {
        if (minute < 0) {
            throw new AppException("days can't be below zero");
        }
        return getNow() + minute * ONEMINUTE;
    }

    public static long getBeforeMinutes(int minute) {
        if (minute < 0) {
            throw new AppException("days can't be below zero");
        }
        return getNow() - minute * ONEMINUTE;
    }

    /**
     * 获取几天后时间点
     *
     * @param days
     * @return
     */
    public static long getAfterDays(int days) {
        if (days < 0) {
            throw new AppException("days can't be below zero");
        }
        return getNow() + days * ONEDAY;
    }

    /**
     * 获取hour小时后时间
     *
     * @param hour
     * @return
     */
    public static long getAfterHours(int hour) {
        if (hour < 0) {
            throw new AppException("hour can't be below zero");
        }
        return getNow() + hour * ONEHOUR;
    }

    /**
     * 获取hour 小时前时间点
     *
     * @param hour
     * @return
     */
    public static long getDelayHours(int hour) {
        if (hour < 0) {
            throw new AppException("hour can't be below zero");
        }
        return getNow() - hour * ONEHOUR;
    }
}
