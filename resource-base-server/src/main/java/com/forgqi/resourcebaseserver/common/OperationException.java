package com.forgqi.resourcebaseserver.common;

public class OperationException{

    public static class NonexistenceException extends RuntimeException {
        public NonexistenceException(String message) {
            super(message);
        }
    }
    public static class UserOperationException extends RuntimeException {
        public UserOperationException(String message) {
            super(message);
        }
    }
}
