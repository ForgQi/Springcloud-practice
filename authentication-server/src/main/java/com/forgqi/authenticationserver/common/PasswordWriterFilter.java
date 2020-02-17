package com.forgqi.authenticationserver.common;

import org.springframework.lang.NonNull;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PasswordWriterFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest httpServletRequest, @NonNull HttpServletResponse httpServletResponse, @NonNull FilterChain filterChain) throws ServletException, IOException {
        if ("POST".equals(httpServletRequest.getMethod()) && new AntPathMatcher().match("/login", httpServletRequest.getServletPath())) {
            filterChain.doFilter(new ChangePasswordHttpServletRequestWrapper(httpServletRequest), httpServletResponse);
        } else {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
    }

    public static class ChangePasswordHttpServletRequestWrapper extends HttpServletRequestWrapper {

        public ChangePasswordHttpServletRequestWrapper(HttpServletRequest request) {
            super(request);
        }

        @Override
        public String getParameter(String name) {
            if ("password".equals(name)) {
                return Encryptors.text("lzu", "deadbeef").encrypt(super.getParameter(name));
            }
            return super.getParameter(name);
        }
    }
}
