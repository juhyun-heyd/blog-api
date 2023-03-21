package com.heyd.blogapi.adapter.in.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResult<T> {

    private final boolean success;
    private final T response;
    private final ApiError errors;

    private ApiResult(boolean success, T response, ApiError errors) {
        this.success = success;
        this.response = response;
        this.errors = errors;
    }

    public static <T> ApiResult<T> ok(T response) {
        return new ApiResult<>(true, response, null);
    }

    public static ApiResult<?> error(Throwable throwable, HttpStatus status) {
        return new ApiResult<>(false, null, new ApiError(throwable, status));
    }
}
