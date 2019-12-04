package com.forgqi.resourcebaseserver.aop;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ControllerAspect {
//
//    @Around("@annotation(com.forgqi.resourcebaseserver.common.Catch)")
//    public Object registryCatch(ProceedingJoinPoint joinPoint) {
//        try {
//            return joinPoint.proceed();
//        } catch (Throwable throwable) {
//            HttpStatus internalServerError = HttpStatus.INTERNAL_SERVER_ERROR;
////            必须在请求上下文中才能获取到
//            HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
//            HashMap<String, Object> map = new LinkedHashMap<>();
//            map.put("timestamp", Instant.now());
//            map.put("status", internalServerError.value());
//            map.put("error", internalServerError.getReasonPhrase());
//            map.put("message", throwable.getMessage());
//            map.put("path", request.getServletPath());
//            return map;
//        }
//    }
}
