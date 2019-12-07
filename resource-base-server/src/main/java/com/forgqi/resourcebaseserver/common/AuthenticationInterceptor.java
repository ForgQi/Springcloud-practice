package com.forgqi.resourcebaseserver.common;

import com.forgqi.resourcebaseserver.common.util.UserHelper;
import com.forgqi.resourcebaseserver.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;

@Slf4j
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!handler.getClass().isAssignableFrom(HandlerMethod.class)) {
            log.warn("cat cast handler to HandlerMethod.class");
            return true;
        }
        // 获取注解
//        Method method = ((HandlerMethod) handler).getMethod();
//        Authorize auth = method.getAnnotation(Authorize.class);
//        if (auth == null) {
//            log.debug("cant find @Authorize in this uri:" + request.getRequestURI());
//            return true;
//        }

        // 从参数中取出用户身份并验证
//        String[] admin = auth.roles();
//        if (!admin.equals(request.getParameter("user"))) {
//            log.debug("permission denied");
//            response.setStatus(403);
//            return false;
//        }
        return true;
    }
}
