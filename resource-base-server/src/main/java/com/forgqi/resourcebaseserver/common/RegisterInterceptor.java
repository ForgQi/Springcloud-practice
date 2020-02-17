package com.forgqi.resourcebaseserver.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgqi.resourcebaseserver.client.LoginFeignClient;
import com.forgqi.resourcebaseserver.client.parse.ParseUtil;
import com.forgqi.resourcebaseserver.common.util.ApplicationContextHelper;
import com.forgqi.resourcebaseserver.common.util.ThreadLocalUtil;
import com.forgqi.resourcebaseserver.common.util.UserHelper;
import com.forgqi.resourcebaseserver.service.dto.UsrPswDTO;
import feign.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static net.logstash.logback.marker.Markers.append;

@Slf4j
public class RegisterInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!handler.getClass().isAssignableFrom(HandlerMethod.class)) {
            log.warn("cat cast handler to HandlerMethod.class");
            return true;
        }
//        String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        UsrPswDTO usrPswDTO = new ObjectMapper().readValue(request.getReader(), UsrPswDTO.class);
        LoginFeignClient loginFeignClient = ApplicationContextHelper.getBean(LoginFeignClient.class);
        assert loginFeignClient != null;
        Response login = loginFeignClient.login(UserHelper.getLoginMap(usrPswDTO.getUserName(), usrPswDTO.getPassword()));
        ThreadLocalUtil.set(usrPswDTO);
        return Optional.of(login).flatMap(ParseUtil::getDocument).map(document -> {
            if (!document.head().getElementsByAttributeValueContaining("href", "login").isEmpty()) {
                log.info(append("user_name", usrPswDTO.getUserName()), "用户名密码错误");
                try {
                    response.sendError(400, "Incorrect password");
                } catch (IOException e) {
                    log.error("RegisterInterceptor,IOException", e);
                }
                return false;
            }
            return true;
        }).orElse(false);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ThreadLocalUtil.remove();
    }
}
