package com.zws.utils.exception;

/**
 * 不支持相关异常
 *
 * @author zhengws
 * @date 2020-12-31 22:12
 */
public class UnSuportExecption extends RuntimeException {
    public UnSuportExecption() {
        super();
    }

    public UnSuportExecption(String message) {
        super(message);
    }

    public UnSuportExecption(String message, Throwable cause) {
        super(message, cause);
    }

    public UnSuportExecption(Throwable cause) {
        super(cause);
    }

    protected UnSuportExecption(String message,
                                Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
