package com.tnnhiu.grocery_management_server.common.exception;

import org.springframework.http.HttpStatusCode;

public interface ErrorCode {
    int getCode();
    String getMessage();
    HttpStatusCode getStatusCode();
}
