package com.tnnhiu.grocery_management_server.modules.identity.exception;

import com.tnnhiu.grocery_management_server.common.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum IdentityErrorCode implements ErrorCode {

    USER_EXISTED(1001, "User already existed", HttpStatus.CONFLICT),
    USER_NOT_FOUND(1002, "User not found", HttpStatus.NOT_FOUND),
    ROLE_NOT_FOUND(1003, "Role not found", HttpStatus.NOT_FOUND);

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    IdentityErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

}
