package com.forgqi.resourcebaseserver.common;

import com.forgqi.resourcebaseserver.entity.User;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserHelper {
    public static Optional<User> getUserBySecurityContext(){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                .map(authentication -> {
                    User user = new User();
                    BeanUtils.copyProperties(authentication.getPrincipal(), user);
                    return user;
                });
    }
    public static Optional<Map<String, String>> getUserLoginMap(){
        return getUserBySecurityContext()
                .map(user -> {
                    Map<String, String> map = new HashMap<>();
                    map.put("Login.Token1", user.getUsername());
                    map.put("Login.Token2", user.getPassword());
                    map.put("goto", "http://my.lzu.edu.cn/loginSuccess.portal");
                    map.put("gotoOnFail", "http://my.lzu.edu.cn/loginFailure.portal");
                    return map;
                });
    }
}
