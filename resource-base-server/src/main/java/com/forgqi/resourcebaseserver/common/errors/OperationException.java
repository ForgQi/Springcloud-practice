package com.forgqi.resourcebaseserver.common.errors;

import org.springframework.http.HttpStatus;

public class OperationException extends AbstractThrowableProblem {

    public OperationException(String message) {
        super(message);
        errorCode = 10;
    }

    public OperationException(String message, Integer errorCode) {
        super(message, errorCode);
    }

    public OperationException(String message, Integer errorCode, HttpStatus httpStatus) {
        super(message, errorCode, httpStatus);
    }
}
