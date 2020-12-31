package com.zws.utils.asserts;

/**
 * @author zhengws
 * @date 2020-12-31 21:28
 */
public class ObjectAssert {
    /**
     * 判空校验
     *
     * @param target
     */
    public static void checkNonNull(Object target) {
        checkNonNull(target, null);
    }

    /**
     * 判空校验
     *
     * @param target
     * @param errorMsg
     */
    public static void checkNonNull(Object target, String errorMsg) {
        if (target == null) {
            if (errorMsg == null) {
                errorMsg = "target is null, please check again.";
            }
            throw new IllegalStateException(errorMsg);
        }
    }
}
