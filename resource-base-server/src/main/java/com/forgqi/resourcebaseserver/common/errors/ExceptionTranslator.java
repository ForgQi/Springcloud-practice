package com.forgqi.resourcebaseserver.common.errors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

import static net.logstash.logback.marker.Markers.append;


@ControllerAdvice
public class ExceptionTranslator {
    private final Logger log = LoggerFactory.getLogger(ExceptionTranslator.class);

    //    @ResponseBody
    @ExceptionHandler(OperationException.class)
    public ResponseEntity exceptionHandler(HttpServletRequest request, OperationException throwable) {
        return throwable.create(request);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity exceptionHandler(HttpServletRequest request, InvalidPasswordException throwable) {
        log.info(append("user_name", throwable.getUsrName()), "用户名密码错误");
        return throwable.create(request);
    }
}
