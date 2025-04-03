package com.sourcery.km.exception;

public class EntityExists extends RuntimeException {
    public EntityExists(String message) {
        super(message);
    }
}
