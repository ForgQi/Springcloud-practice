package com.forgqi.resourcebaseserver.common.errors;

public class NonexistenceException extends RuntimeException {
    public NonexistenceException(String message) {
        super(message);
    }
}
