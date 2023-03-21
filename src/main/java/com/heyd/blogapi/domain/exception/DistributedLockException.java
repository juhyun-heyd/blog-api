package com.heyd.blogapi.domain.exception;

public class DistributedLockException extends BusinessException {

    public DistributedLockException(int statusCode, String message) {
        super(statusCode, message);
    }
}
