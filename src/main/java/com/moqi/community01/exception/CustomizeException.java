package com.moqi.community01.exception;

/**
 * Created by codedrinker on 2019/5/28.
 */
public class CustomizeException extends RuntimeException {
    private Integer code;
    private String message;

    public CustomizeException(ICustomizeErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }
}
