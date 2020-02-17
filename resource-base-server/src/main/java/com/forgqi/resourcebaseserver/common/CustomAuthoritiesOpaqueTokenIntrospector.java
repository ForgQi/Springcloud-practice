package com.forgqi.resourcebaseserver.common;

import com.forgqi.resourcebaseserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.NimbusOpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionClaimNames;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomAuthoritiesOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

    private final OpaqueTokenIntrospector delegate;
    private final UserRepository userRepository;

    public CustomAuthoritiesOpaqueTokenIntrospector(
            @Value("${spring.security.oauth2.resourceserver.opaque-token.introspection-uri}") String introspectionUri,
            @Value("${spring.security.oauth2.resourceserver.opaque-token.client-id}") String clientId,
            @Value("${spring.security.oauth2.resourceserver.opaque-token.client-secret}") String clientSecret,
            UserRepository userRepository) {
        this.userRepository = userRepository;
        this.delegate =
                new NimbusOpaqueTokenIntrospector(introspectionUri, clientId, clientSecret);

    }

    public OAuth2AuthenticatedPrincipal introspect(String token) {
        OAuth2AuthenticatedPrincipal principal = this.delegate.introspect(token);
        String userName = principal.getAttribute("user_name");
//        System.out.println(userName+"????????");
        assert userName != null;
        return userRepository.findById(Long.valueOf(userName)).map(user -> {
            user.setAttributes(principal.getAttributes());
            return user;
        }).get();
//        return new DefaultOAuth2AuthenticatedPrincipal(
//                principal.getName(), principal.getAttributes(), extractAuthorities(principal));
    }

    private Collection<GrantedAuthority> extractAuthorities(OAuth2AuthenticatedPrincipal principal) {
        List<String> scopes = principal.getAttribute(OAuth2IntrospectionClaimNames.SCOPE);
        assert scopes != null;
        return scopes.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
