package com.forgqi.resourcebaseserver.common.errors;

import org.springframework.http.HttpStatus;

public class InvalidPasswordException extends AbstractThrowableProblem {

    private String usrName;

    private InvalidPasswordException() {
        super("Incorrect password");
        httpStatus = HttpStatus.BAD_REQUEST;
    }

    public InvalidPasswordException(String usrName) {
        super("Incorrect password");
        this.usrName = usrName;
        httpStatus = HttpStatus.BAD_REQUEST;
    }

    public String getUsrName() {
        return usrName;
    }
}
