package com.sourcery.km.exception;

import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({MainException.class})
    public ProblemDetail handleMainException (MainException exception) {
        ProblemDetail response = ProblemDetail.forStatus(exception.status);
        response.setTitle(exception.title);
        response.setDetail(exception.getMessage());
        return response;
    }
}
