package com.heyd.blogapi.domain.exception;

public class RedisZSetOperationException extends BusinessException {

    public RedisZSetOperationException(int statusCode, String message) {
        super(statusCode, message);
    }
}
