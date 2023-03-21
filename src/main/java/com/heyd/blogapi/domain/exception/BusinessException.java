package com.heyd.blogapi.domain.exception;

public class BusinessException extends RuntimeException {

    private final int statusCode;

    public BusinessException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

}
