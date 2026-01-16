package com.tnnhiu.grocery_management_server.common.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    int getCode();

    String getMessage();

    HttpStatus getHttpStatusCode();
}
