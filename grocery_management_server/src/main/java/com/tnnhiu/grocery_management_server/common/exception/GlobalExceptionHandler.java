package com.tnnhiu.grocery_management_server.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnwantedException(Exception e) {
        log.error("Unknown error occurred: ", e);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error."
        );
        problemDetail.setProperty("code", 9999);
        return problemDetail;
    }

    @ExceptionHandler(AppException.class)
    public ProblemDetail handleAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                errorCode.getStatusCode(),
                errorCode.getMessage()
        );
        problemDetail.setProperty("code", errorCode.getCode());

        return problemDetail;
    }

    @Override
    protected @Nullable ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                status, "Validation failed."
        );
        Map<String, String> errors = exception.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,           // Key: tên trường (vd: username)
                        // Value: thông báo lỗi (vd: Username is required), xử lý null an toàn
                        error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : "Invalid value",
                        (existing, replacement) -> existing // Merge function: Nếu có nhiều lỗi trên 1 field, chỉ lấy cái đầu tiên
                ));

        problemDetail.setProperty("errors", errors);
        return createResponseEntity(problemDetail, headers, status, request);
    }
}
