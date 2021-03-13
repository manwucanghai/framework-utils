package com.zws.utils.asserts;

import com.zws.utils.tools.StringUtils;

import java.util.Collection;

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
                errorMsg = "field can't be null";
            }
            throw new IllegalStateException(errorMsg);
        }
    }

    /**
     * 校验字符串格式
     *
     * @param content
     * @param regex
     * @param expect
     */
    public static void checkStrDivNum(String content, String regex, int expect) {
        if (StringUtils.isBlank(content)) {
            throw new IllegalStateException("content is blank.");
        }
        String[] strings = content.split(regex);
        if (strings.length != expect) {
            throw new IllegalStateException("Illegal content format.");
        }
    }

    /**
     * 校验两值是否相等
     *
     * @param expect
     * @param target
     */
    public static void checkEqual(Object expect, Object target) {
        checkEqual(expect, target, null);
    }

    /**
     * 校验两值是否相等
     *
     * @param expect
     * @param target
     * @return
     */
    public static void checkEqual(Object expect, Object target, String errorMsg) {
        if (!expect.equals(target)) {
            if (errorMsg == null) {
                errorMsg = "Two values are not equal";
            }
            throw new IllegalStateException(errorMsg);
        }
    }


    public static void checkContainsThrowError(Collection<?> collection, Object match) {
        checkContainsThrowError(collection, match, null);
    }

    /**
     * 校验，包含抛异常.
     *
     * @param collection
     * @param match
     */
    public static void checkContainsThrowError(Collection<?> collection, Object match, String errorMsg) {
        if (collection.contains(match)) {
            if (errorMsg == null) {
                errorMsg = "Object verification failed, including illegal objects.";
            }
            throw new IllegalStateException(errorMsg);
        }
    }


    public static void checkNotContainsThrowError(Collection<?> collection, Object match) {
        checkNotContainsThrowError(collection, match, null);
    }

    /**
     * 校验，不包含抛异常.
     *
     * @param collection
     * @param match
     */
    public static void checkNotContainsThrowError(Collection<?> collection, Object match, String errorMsg) {
        if (!collection.contains(match)) {
            if (errorMsg == null) {
                errorMsg = "Object verification failed, illegal objects.";
            }
            throw new IllegalStateException(errorMsg);
        }
    }

    /**
     * 校验并抛异常.
     *
     * @param flag
     * @param errorMsg
     */
    public static void checkThrowError(boolean flag, String errorMsg) {
        if (flag) {
            if (errorMsg == null) {
                errorMsg = "";
            }
            throw new IllegalStateException(errorMsg);
        }
    }


    public static void checkNonEmpty(Collection<?> targets) {
        checkNonEmpty(targets, null);
    }

    /**
     * 判空校验
     *
     * @param targets
     * @param errorMsg
     */
    public static void checkNonEmpty(Collection<?> targets, String errorMsg) {
        if (targets == null || targets.isEmpty()) {
            if (errorMsg == null) {
                errorMsg = "targets can't be empty";
            }
            throw new IllegalStateException(errorMsg);
        }
    }

    public static void checkNonEmpty(String content) {
        checkNonEmpty(content, null);
    }

    /**
     * 判空校验
     *
     * @param content
     * @param errorMsg
     */
    public static void checkNonEmpty(String content, String errorMsg) {
        if (StringUtils.isBlank(content)) {
            if (errorMsg == null) {
                errorMsg = "targets can't be empty";
            }
            throw new IllegalStateException(errorMsg);
        }
    }
}
