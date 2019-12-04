package com.forgqi.resourcebaseserver.common.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedHashMap;

public abstract class AbstractThrowableProblem extends RuntimeException {
    // 自定义错误码暂时没用
    private Integer errorCode;



    private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    AbstractThrowableProblem(String message) {
        super(message);
    }
    AbstractThrowableProblem(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
    AbstractThrowableProblem(String message, Integer errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    AbstractThrowableProblem(String message, Integer errorCode, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }

    int getHttpStatusCode() {
        return httpStatus.value();
    }

    public int getErrorCode() {
        return errorCode;
    }

    String getReasonPhrase() {
        return httpStatus.getReasonPhrase();
    }

    public ResponseEntity create(HttpServletRequest request) {
        HashMap<String, Object> map = new LinkedHashMap<>();
        map.put("timestamp", Instant.now());
        map.put("status", getHttpStatusCode());
        map.put("error", getReasonPhrase());
        map.put("message", getMessage());
        map.put("path", request.getServletPath());
        return new ResponseEntity<>(map, httpStatus);
    }
}
