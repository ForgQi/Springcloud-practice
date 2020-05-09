package com.forgqi.resourcebaseserver;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.forgqi.resourcebaseserver.entity.SysRole;
import com.forgqi.resourcebaseserver.entity.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class TestUtil {

    private static final ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapper.registerModule(new JavaTimeModule());
    }

    /**
     * Convert an object to JSON byte array.
     *
     * @param object the object to convert.
     * @return the JSON byte array.
     * @throws IOException ioErr
     */
    public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        return mapper.writeValueAsBytes(object);
    }

    @Retention(RetentionPolicy.RUNTIME)
    @WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
    public @interface WithMockCustomUser {

        long username() default 320160936051L;

        String[] roles() default {"ADMIN"};
    }

    public static class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {
        @Override
        public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            User principal = new User();
            principal.setId(customUser.username());
            principal.setRoles(Stream.of(customUser.roles()).map(SysRole::new).collect(Collectors.toUnmodifiableList()));
            Authentication auth =
                    new UsernamePasswordAuthenticationToken(principal, "password", principal.getAuthorities());
            context.setAuthentication(auth);
            return context;
        }
    }
}
