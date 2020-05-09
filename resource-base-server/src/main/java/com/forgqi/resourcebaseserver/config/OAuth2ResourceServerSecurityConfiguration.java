package com.forgqi.resourcebaseserver.config;

import com.forgqi.resourcebaseserver.entity.User;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.servlet.http.HttpServletResponse;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
public class OAuth2ResourceServerSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.cors(withDefaults());
        http
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .antMatchers("/v1/user/*").hasRole("SUPER")
                                .antMatchers("/v1/**").authenticated()
                                .anyRequest().permitAll()
                )
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer -> {
                    OAuth2ResourceServerConfigurer.opaqueToken();
                    OAuth2ResourceServerConfigurer.authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED));
                })
                .oauth2Login(oauth2Login ->
                        oauth2Login.userInfoEndpoint(userInfoEndpoint ->
                                userInfoEndpoint
                                        .customUserType(User.class, "opaque-token")));

    }

//    private OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
//        final OAuth2UserService delegate = new OAuth2UserService();
//        final OidcUserService delegate = new OidcUserService();
//
//        return (userRequest) -> {
//            // Delegate to the default implementation for loading a user
//            OidcUser oidcUser = delegate.loadUser(userRequest);
//
//            OAuth2AccessToken accessToken = userRequest.getAccessToken();
//            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
//
//            // TODO
//            // 1) Fetch the authority information from the protected resource using accessToken
//            // 2) Map the authority information to one or more GrantedAuthority's and add it to mappedAuthorities
//
//            // 3) Create a copy of oidcUser but use the mappedAuthorities instead
//            oidcUser = new DefaultOidcUser(mappedAuthorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
//
//            return oidcUser;
//    }
    //@EnableResourceServer
//    @Configuration
//    // 数字越小优先级越高
//    @Order(99)
//    public static class ResourceServerConfig extends WebSecurityConfigurerAdapter {
//        //
//        //    @Bean
//        //    public RedisTokenStore tokenStore(RedisConnectionFactory connectionFactory) {
//        //        return new RedisTokenStore(connectionFactory);
//        //    }
//        ////    // 远程连接authServer服务
//        ////    @Autowired
//        ////    public RemoteTokenServices remoteTokenServices;
//        ////
//        ////    @Override
//        ////    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
//        ////        resources.tokenServices(remoteTokenServices);
//        ////    }
//        //
//            @Override
//            public void configure(HttpSecurity http) throws Exception {
//                //            http.csrf().disable();
//            http
//                    .antMatcher("/v1/**")
//                    .requestMatchers().antMatchers("/v1/**")
//                    .and()
//
////                .csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
////                        .and()
//        //            .and()
//        //                .authorizeRequests().antMatchers("/actuator/**").permitAll()
//            }
//        //
//        //    @Bean
//        //    public DefaultTokenServices defaultTokenServices(RedisTokenStore tokenStore) {
//        //        DefaultTokenServices tokenServices = new DefaultTokenServices();
//        //        tokenServices.setTokenStore(tokenStore);
//        //
//        //        return tokenServices;
//        //    }
//    }
}
