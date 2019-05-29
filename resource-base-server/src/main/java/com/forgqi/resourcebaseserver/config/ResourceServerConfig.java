package com.forgqi.resourcebaseserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.servlet.http.HttpServletResponse;

/**
 * 〈资源认证服务器〉
 * 配置资源服务器
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Autowired
    private RedisConnectionFactory connectionFactory;

    @Bean
    public RedisTokenStore tokenStore() {
        return new RedisTokenStore(connectionFactory);
    }
//    // 远程连接authServer服务
//    @Autowired
//    public RemoteTokenServices remoteTokenServices;
//
//    @Override
//    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
//        resources.tokenServices(remoteTokenServices);
//    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .and()
                .requestMatchers().antMatchers("/api/**")
//            .and()
//                .authorizeRequests().antMatchers("/actuator/**").permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/api/user/*").hasRole("SUPER")
                .and()
                .authorizeRequests()
                .antMatchers("/api/**").authenticated();
//            .and()
//                .httpBasic();
    }

    @Bean
    public DefaultTokenServices defaultTokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(tokenStore());

        return tokenServices;
    }
}
