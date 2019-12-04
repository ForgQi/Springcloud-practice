package com.forgqi.resourcebaseserver.common.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class InvalidPasswordException extends AbstractThrowableProblem {

    private String usrName;
    private InvalidPasswordException(){
        super("Incorrect password", HttpStatus.BAD_REQUEST);
    }
    public InvalidPasswordException(String usrName) {
        super("Incorrect password", HttpStatus.BAD_REQUEST);
        this.usrName = usrName;
    }

    public String getUsrName() {
        return usrName;
    }
}
