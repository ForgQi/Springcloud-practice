package com.forgqi.resourcebaseserver.common.errors;

public class UnsupportedOperationException extends AbstractThrowableProblem {
    public UnsupportedOperationException(String message) {
        super(message);
    }

    public UnsupportedOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
