package com.zws.utils.id;

/**
 * @author zhengws
 * @date 2020-09-07 19:32
 */
public interface IKeyGenerator {
    /**
     * 生成下一个key
     *
     * @return
     */
    long nextKey();

    /**
     * 生成下一个key，string
     *
     * @return
     */
    String nextStrKey();

    /**
     * 生成下一个key
     *
     * @return
     */
    long nextKey(Object type);

    /**
     * 生成下一个key，string
     *
     * @return
     */
    String nextStrKey(Object type);
}
