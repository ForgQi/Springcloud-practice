package com.forgqi.authenticationserver.common;

import com.forgqi.authenticationserver.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import java.util.Objects;
import java.util.function.Consumer;

@Component
public class UserHelper {
    private static
    TokenStore tokenStore;
    private static
    ResourceServerTokenServices tokenServices;

    //     修改id所指用户
    public static void reloadUserFromSecurityContext(Consumer<User> consumer, Long id) {
        reloadUserFromSecurityContext(String.valueOf(id), consumer);
    }

    //     修改当前会话的用户
    public static void reloadUserFromSecurityContext(Consumer<com.forgqi.authenticationserver.entity.User> consumer) {
        reloadUserFromSecurityContext(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername(), consumer);
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
