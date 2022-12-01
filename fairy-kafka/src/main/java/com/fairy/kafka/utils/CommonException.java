package com.fairy.kafka.utils;

public class CommonException extends RuntimeException {
    public CommonException(String message, Throwable cause) {
        super(message, cause);
    }

    public static CommonException create(Throwable e) {
        return new CommonException(e.getMessage(), e);
    }
}
