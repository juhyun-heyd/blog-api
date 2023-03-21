package com.heyd.blogapi.domain.exception;

public class WebClientBadResponseException extends BusinessException {

    public WebClientBadResponseException(int statusCode, String message) {
        super(statusCode, message);
    }
}
