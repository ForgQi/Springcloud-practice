package com.forgqi.resourcebaseserver.config;

import com.forgqi.resourcebaseserver.common.CustomAuthoritiesOpaqueTokenIntrospector;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

import javax.servlet.http.HttpServletResponse;

@EnableWebSecurity
public class OAuth2ResourceServerSecurityConfiguration extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .and()
                .requestMatchers().antMatchers("/v1/**")
                .and()
                .authorizeRequests()
                .antMatchers("/v1/user/*").hasRole("SUPER")
                .and()
                .authorizeRequests()
                .antMatchers("/v1/**").authenticated()
                .and()
            .oauth2ResourceServer()
                .opaqueToken();
//                .introspectionUri("localhost:8081/oauth/check_token")
//                .introspectionClientCredentials("Lzu", "196460cfce2638a2529568978b068340b9adb02e447be36378e8c13e8030880a");
    }
}
