package com.sourcery.km.exception;

import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({EntityExists.class})
    public ProblemDetail handleUserNotFound (EntityExists exception) {
        ProblemDetail response = ProblemDetail.forStatus(409);
        response.setTitle("User already exists");
        response.setDetail(exception.getMessage());
        return response;
    }

    @ExceptionHandler({UnauthorizedException.class})
    public ProblemDetail handleUnauthorizedException (UnauthorizedException exception) {
        ProblemDetail response = ProblemDetail.forStatus(403);
        response.setTitle("Unauthorized action");
        response.setDetail(exception.getMessage());
        return response;
    }

    @ExceptionHandler({EntityNotFound.class})
    public ProblemDetail handleNotFound (EntityNotFound exception) {
        ProblemDetail response = ProblemDetail.forStatus(404);
        response.setTitle("Not found");
        response.setDetail(exception.getMessage());
        return response;
    }
}
