package com.forgqi.adminserver;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.UUID;

/**
 * proxyBeanMethods属性：默认是开启的，
 * 开启后允许其它配置类调用这个类的@bean方法
 * 指定是一个配置列，关了proxyBeanMethods，其它配置类就不能调相应的@bean类
 */
@Configuration(proxyBeanMethods = false)
@EnableAdminServer
public class SecuritySecureConfig extends WebSecurityConfigurerAdapter {

    private final AdminServerProperties adminServer;
    private final String user;
    private final String psw;

    public SecuritySecureConfig(AdminServerProperties adminServer, @Value("${spring.security.user.name}") String user, @Value("${spring.security.user.password}") String psw) {
        this.adminServer = adminServer;
        this.user = user;
        this.psw = psw;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setTargetUrlParameter("redirectTo");
        successHandler.setDefaultTargetUrl(this.adminServer.path("/"));

        http.authorizeRequests()
                // Grants public access to all static assets and the login page.
                .antMatchers(this.adminServer.path("/assets/**")).permitAll()
                .antMatchers(this.adminServer.path("/login")).permitAll()
                // Every other request must be authenticated.
                .anyRequest().authenticated()
                .and()
                // Configures login and logout.
                .formLogin().loginPage(this.adminServer.path("/login")).successHandler(successHandler).and()
                .logout().logoutUrl(this.adminServer.path("/logout")).and()
                // Enables HTTP-Basic support. This is needed for the Spring Boot Admin Client to register.
                .httpBasic().and()
                .csrf()
                // Enables CSRF-Protection using Cookies
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .ignoringRequestMatchers(
                        // Disables CSRF-Protection for the endpoint the Spring Boot Admin Client uses to (de-)register.
                        new AntPathRequestMatcher(this.adminServer.path("/instances"), HttpMethod.POST.toString()),
                        new AntPathRequestMatcher(this.adminServer.path("/instances/*"), HttpMethod.DELETE.toString()),
                        // Disables CSRF-Protection for the actuator endpoints.
                        new AntPathRequestMatcher(this.adminServer.path("/actuator/**"))
                )
                .and()
                .rememberMe().key(UUID.randomUUID().toString()).tokenValiditySeconds(1209600);
        // @formatter:on
    }

    // Required to provide UserDetailsService for "remember functionality"
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser(user).password("{noop}" + psw).roles("USER");
    }
}