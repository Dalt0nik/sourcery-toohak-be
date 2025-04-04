package com.sourcery.km.exception;

import org.springframework.http.HttpStatus;

public abstract class MainException extends RuntimeException {
    String title;
    HttpStatus status;

    public MainException(String message) {
        super(message);
    }

    public MainException(String message, String title, HttpStatus status) {
        super(message);
        this.title = title;
        this.status = status;
    }
}
