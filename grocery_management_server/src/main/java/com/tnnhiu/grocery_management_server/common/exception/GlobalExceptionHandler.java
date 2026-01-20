package com.tnnhiu.grocery_management_server.common.exception;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
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
        return buildProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", "Internal Server Error", null);
    }

    @ExceptionHandler(AppException.class)
    public ProblemDetail handleAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        return buildProblemDetail(errorCode.getStatusCode(), errorCode.getMessage(), "Application Error", errorCode.getCode());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail handleAuthenticationException(AuthenticationException e) {
        return buildProblemDetail(HttpStatus.UNAUTHORIZED, e.getMessage(), "Authentication Error", null);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDeniedException(AccessDeniedException e) {
        return buildProblemDetail(HttpStatus.FORBIDDEN, e.getMessage(), "Access Denied", null);
    }

    private ProblemDetail buildProblemDetail(HttpStatusCode status, String detail, String title, Integer code) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setTitle(title);
        if (code != null) {
            problemDetail.setProperty("code", code);
        }
        return problemDetail;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {

        ProblemDetail problemDetail = exception.getBody();

        Map<String, String> errors = exception.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        error -> error.getDefaultMessage() != null ?
                                error.getDefaultMessage() : "Invalid value",
                        (existing, replacement) -> existing
                ));

        problemDetail.setProperty("errors", errors);
        return createResponseEntity(problemDetail, headers, status, request);
    }
}
