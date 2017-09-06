package com.lefore.ssm.exception;

/**
 * author: lefore
 * date: 2017/8/24
 * email: 862080515@qq.com
 */
public class BizException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public BizException(String message) {
        super(message);
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
    }
}
