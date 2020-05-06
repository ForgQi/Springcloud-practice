package com.forgqi.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http,
                                                            ReactiveClientRegistrationRepository clientRegistrationRepository) {
        // Authenticate through configured OpenID Provider
        http.oauth2Login();
        http.oauth2ResourceServer().opaqueToken();
        // Require authentication for all requests
        http.authorizeExchange()
                .pathMatchers("/v1/**").authenticated()
                .anyExchange().permitAll();
        // Disable CSRF in the gateway to prevent conflicts with proxied service CSRF
        http.csrf().disable();
//        http.cors(withDefaults());
        return http.build();
    }

}

@Configuration
@Profile({"dev"})
class CorsConfig {

    // 这种方法配置之后再使用自定义拦截器时跨域相关配置就会失效。
    // 原因是请求经过的先后顺序问题，当请求到来时会先进入拦截器中，
    // 而不是进入Mapping映射中，所以返回的头信息中并没有配置的跨域信息。
    // 浏览器就会报跨域异常。所以需要在security中配置cors()
    @Bean
    public CorsWebFilter corsFilter() {
//        System.out.println("?????????????");
//        final CorsConfiguration corsConfiguration = new CorsConfiguration();
//        /*是否允许请求带有验证信息*/
//        corsConfiguration.setAllowCredentials(true);
//        /*允许访问的客户端域名*/
//        corsConfiguration.addAllowedOrigin("*");
//        /*允许服务端访问的客户端请求头*/
//        corsConfiguration.addAllowedHeader("*");
//        /*允许访问的方法名,GET POST等*/
//        corsConfiguration.addAllowedMethod("*");
//        final UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
//
//        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
//        return new CorsWebFilter(urlBasedCorsConfigurationSource);
        return new CorsWebFilter(corsConfigurationSource());
    }

    // gateway中必须要此bean和corsFilter，其他项目中只使用corsFilter也可，不知道原因
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration().applyPermitDefaultValues();
        config.addAllowedMethod("*");
        config.setAllowCredentials(true);
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
