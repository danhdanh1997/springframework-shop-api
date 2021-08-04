package com.xuandanh.springbootshop.advice;

import java.util.Date;

import com.xuandanh.springbootshop.exception.TokenRefreshException;
import com.xuandanh.springbootshop.payload.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
@RestControllerAdvice
public class TokenControllerAdvice {

    @ExceptionHandler(value = TokenRefreshException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResponse handleTokenRefreshException(TokenRefreshException ex, WebRequest request) {
        return new ApiResponse(true, "HttpStatus.FORBIDDEN.value()", ex.getMessage(), request.getDescription(false));
    }
}