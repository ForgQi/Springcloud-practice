package com.forgqi.resourcebaseserver.common.util;

import com.forgqi.resourcebaseserver.client.parse.SsoParse;
import com.forgqi.resourcebaseserver.common.errors.OperationException;
import com.forgqi.resourcebaseserver.entity.SysRole;
import com.forgqi.resourcebaseserver.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
public class UserHelper {
    private final static Logger log = LoggerFactory.getLogger(UserHelper.class);
    private final static
    TextEncryptor textEncryptor = Encryptors.text("lzu", "deadbeef");
    private static
    TokenStore tokenStore;
    private static
    ResourceServerTokenServices tokenServices;
    private static SsoParse ssoParse;

    public static Optional<User> getUserBySecurityContext() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                .map(authentication -> (com.forgqi.authenticationserver.entity.User) authentication.getPrincipal())
                .map(principal -> {
                    User user = new User();
                    BeanUtils.copyProperties(principal, user);
                    user.setRoles(principal.getRoles().stream()
                            .map(sysRole -> new SysRole(sysRole.getId(), sysRole.getRole()))
                            .collect(Collectors.toUnmodifiableList()));
                    return user;
                });
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

    // 修改id所指用户
    public static void reloadUserFromSecurityContext(Consumer<com.forgqi.authenticationserver.entity.User> consumer, Long id) {
        reloadUserFromSecurityContext(String.valueOf(id), consumer);
    }

    // 修改当前会话的用户
    public static void reloadUserFromSecurityContext(Consumer<com.forgqi.authenticationserver.entity.User> consumer) {
        reloadUserFromSecurityContext(getUserBySecurityContext().map(User::getUsername).get(), consumer);
    }

    private static void reloadUserFromSecurityContext(String username, Consumer<com.forgqi.authenticationserver.entity.User> consumer) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        Authentication authentication = new BearerTokenExtractor().extract(request);
        String token = (String) authentication.getPrincipal();
        Collection<OAuth2AccessToken> tokenCollection = tokenStore
                .findTokensByClientIdAndUserName(tokenServices.loadAuthentication(token).getOAuth2Request().getClientId(), username);
        for (OAuth2AccessToken oToken : tokenCollection) {
//            System.out.println(oToken);
            OAuth2Authentication requestingUser = tokenStore.readAuthentication(oToken);
            if (requestingUser == null) {
                tokenStore.removeAccessToken(oToken);
                continue;
            }
            Authentication userAuthentication = requestingUser.getUserAuthentication();
            com.forgqi.authenticationserver.entity.User principal = (com.forgqi.authenticationserver.entity.User) userAuthentication.getPrincipal();
            consumer.accept(principal);

            OAuth2Request ooAuth2Request = requestingUser.getOAuth2Request();
            OAuth2Request oAuth2Request = new OAuth2Request(ooAuth2Request.getRequestParameters(), ooAuth2Request.getClientId(), principal.getAuthorities(), ooAuth2Request.isApproved(), ooAuth2Request.getScope(), ooAuth2Request.getResourceIds(), ooAuth2Request.getRedirectUri(), ooAuth2Request.getResponseTypes(), ooAuth2Request.getExtensions());
            OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, new UsernamePasswordAuthenticationToken(principal, userAuthentication.getCredentials(), principal.getAuthorities()));

            tokenStore.storeAccessToken(oToken, oAuth2Authentication);
            tokenStore.storeRefreshToken(tokenServices.readAccessToken(oToken.getValue()).getRefreshToken(), oAuth2Authentication);
        }

    }

    public User getUser() {
        return getUserBySecurityContext().orElseThrow();
    }

    @Autowired
    public void setSsoParse(SsoParse ssoParse) {
        UserHelper.ssoParse = ssoParse;
    }

    @Autowired
    @Qualifier("tokenStore")
    public void setTokenStore(TokenStore tokenStore) {
        UserHelper.tokenStore = tokenStore;
    }

    @Autowired
    @Qualifier("defaultTokenServices")
    public void setTokenServices(ResourceServerTokenServices tokenServices) {
        UserHelper.tokenServices = tokenServices;
    }
}
