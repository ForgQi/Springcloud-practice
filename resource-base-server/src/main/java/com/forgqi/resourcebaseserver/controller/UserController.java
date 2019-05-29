package com.forgqi.resourcebaseserver.controller;

import com.forgqi.resourcebaseserver.entity.SysRole;
import com.forgqi.resourcebaseserver.entity.User;
import com.forgqi.resourcebaseserver.repository.SysRoleRepository;
import com.forgqi.resourcebaseserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    SysRoleRepository sysRoleRepository;
    @Autowired
    @Qualifier("tokenStore")
    TokenStore tokenStore;
    @Autowired
    @Qualifier("defaultTokenServices")
    ResourceServerTokenServices tokenServices;

//    @Value("${spring.application.name}")
//    String server;

    @RequestMapping(value = "/user/{id}", method = RequestMethod.PUT)
//    public String home(@RequestParam(value = "name", defaultValue = "World") String name)
    public User role(@PathVariable Long id, @RequestBody List<SysRole> sysRoles, ServletRequest req) {
//        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
//        String name = userDetails.getUsername();
        final HttpServletRequest request = (HttpServletRequest) req;
        Authentication authentication = new BearerTokenExtractor().extract(request);
//        System.out.println(authentication);
        List<SysRole> roles = new ArrayList<>();
        for (SysRole sysRole :
                sysRoles) {
            roles.add(sysRoleRepository.findFirstByName(sysRole.getName()));
        }
        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        reloadUserFromSecurityContext(authentication, roles, id);
        user.setRoles(roles);
        return userRepository.save(user);
    }

    private void reloadUserFromSecurityContext(Authentication authentication, List<SysRole> roles, Long id) {
        String token = (String) authentication.getPrincipal();
        Collection<OAuth2AccessToken> tokenCollection = tokenStore
                .findTokensByClientIdAndUserName(tokenServices.loadAuthentication(token).getOAuth2Request().getClientId(), String.valueOf(id));
        for (OAuth2AccessToken oToken : tokenCollection) {
//            System.out.println(oToken);
            OAuth2Authentication requestingUser = tokenStore.readAuthentication(oToken);
            if (requestingUser == null) {
                tokenStore.removeAccessToken(oToken);
                continue;
            }
            Authentication userAuthentication = requestingUser.getUserAuthentication();
            com.forgqi.authenticationserver.entity.User principal = (com.forgqi.authenticationserver.entity.User) userAuthentication.getPrincipal();
            List<com.forgqi.authenticationserver.entity.SysRole> sysRoleList = new ArrayList<>();
            for (SysRole role :
                    roles) {
                com.forgqi.authenticationserver.entity.SysRole sysRole = new com.forgqi.authenticationserver.entity.SysRole(role.getName());
                sysRole.setId(role.getId());
                sysRoleList.add(sysRole);
            }
            principal.setRoles(sysRoleList);
            OAuth2Request ooAuth2Request = requestingUser.getOAuth2Request();
            OAuth2Request oAuth2Request = new OAuth2Request(ooAuth2Request.getRequestParameters(), ooAuth2Request.getClientId(), principal.getAuthorities(), ooAuth2Request.isApproved(), ooAuth2Request.getScope(), ooAuth2Request.getResourceIds(), ooAuth2Request.getRedirectUri(), ooAuth2Request.getResponseTypes(), ooAuth2Request.getExtensions());
            OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, new UsernamePasswordAuthenticationToken(principal, userAuthentication.getCredentials(), principal.getAuthorities()));

            tokenStore.storeAccessToken(oToken, oAuth2Authentication);
            tokenStore.storeRefreshToken(tokenServices.readAccessToken(oToken.getValue()).getRefreshToken(), oAuth2Authentication);
        }

    }
}
