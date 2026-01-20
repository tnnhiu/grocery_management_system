package com.tnnhiu.grocery_management_server.modules.identity.exception;



import com.tnnhiu.grocery_management_server.common.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum IdentityErrorCode implements ErrorCode {

    USER_EXISTED(1001, "User already existed", HttpStatus.CONFLICT),
    USER_NOT_FOUND(1002, "User not found", HttpStatus.NOT_FOUND),
    ROLE_NOT_FOUND(1003, "Role not found", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1004, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    JWT_GENERATE_ERROR(1005, "JWT generate error", HttpStatus.INTERNAL_SERVER_ERROR),
    JWT_INVALID(1006, "JWT invalid", HttpStatus.UNAUTHORIZED);


    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    IdentityErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }


}
