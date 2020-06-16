package com.forgqi.resourcebaseserver.common.util;

import com.forgqi.resourcebaseserver.client.parse.SsoParse;
import com.forgqi.resourcebaseserver.common.errors.OperationException;
import com.forgqi.resourcebaseserver.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class UserHelper {
    private final static
    TextEncryptor textEncryptor = Encryptors.text("lzu", "deadbeef");

    private static SsoParse ssoParse;

    public static Optional<User> getUserBySecurityContext() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication()).map(authentication -> {
            if ("anonymousUser".equals(authentication.getPrincipal())) {
                return null;
            }
            return (User) authentication.getPrincipal();
        });
//                .map(principal -> {
//                    User user = new User();
//                    BeanUtils.copyProperties(principal, user);
//                    user.setRoles(principal.getRoles().stream()
//                            .map(sysRole -> new SysRole(sysRole.getId(), sysRole.getRole()))
//                            .collect(Collectors.toUnmodifiableList()));
//                    return user;
//                });
    }

    public static long getUserIdBySecurityContext() {
        return getUserBySecurityContext()
                .map(User::getId).orElseThrow();
    }

    public static Optional<Map<String, String>> getUserLoginMap() {
        return getUserBySecurityContext()
                .map(user -> getLoginMap(user.getUsername(), user.getPassword()));
    }

    public static Map<String, String> getLoginMap(String userName, String password) {
        try {
            Map<String, String> loginMap = ssoParse.getLoginMap();
            loginMap.put("username", userName);
            loginMap.put("password", textEncryptor.decrypt(password));
            return loginMap;
        } catch (NullPointerException e) {
            log.warn(e.getMessage(), e);
            throw new OperationException("登录出错请稍后再试");
        }
//        Map<String, String> map = new HashMap<>();
//        map.put("Login.Token1", userName);
//        map.put("Login.Token2", textEncryptor.decrypt(password));
//        map.put("goto", "http://my.lzu.edu.cn/loginSuccess.portal");
//        map.put("gotoOnFail", "http://my.lzu.edu.cn/loginFailure.portal");
    }

    public User getUser() {
        return getUserBySecurityContext().orElseThrow();
    }

    @Autowired
    public void setSsoParse(SsoParse ssoParse) {
        UserHelper.ssoParse = ssoParse;
    }
}
