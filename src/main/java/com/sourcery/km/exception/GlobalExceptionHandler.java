package com.sourcery.km.exception;

import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({QuizNotFoundException.class})
    public ProblemDetail handleQuizNotFoundException (QuizNotFoundException exception){
        ProblemDetail response = ProblemDetail.forStatus(404);
        response.setTitle("Quiz not found");
        response.setDetail(exception.getMessage());
        return response;
    }

    @ExceptionHandler({UserNotFound.class})
    public ProblemDetail handleUserNotFound (UserNotFound exception){
        ProblemDetail response = ProblemDetail.forStatus(404);
        response.setTitle("User not found");
        response.setDetail(exception.getMessage());
        return response;
    }

    @ExceptionHandler({UserAlreadyExists.class})
    public ProblemDetail handleUserNotFound (UserAlreadyExists exception){
        ProblemDetail response = ProblemDetail.forStatus(409);
        response.setTitle("User already exists");
        response.setDetail(exception.getMessage());
        return response;
    }
}
