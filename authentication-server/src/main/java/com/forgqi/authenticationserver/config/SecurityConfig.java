package com.forgqi.authenticationserver.config;

import com.forgqi.authenticationserver.service.CustomUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
//@EnableWebSecurity 没发现作用
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomUserDetailService userDetailService;

    public SecurityConfig(CustomUserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        //return new BCryptPasswordEncoder();
        return new AESEncryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//                .and()
//                .requestMatchers().antMatchers("/actuator/**")
//                .and()
//                .authorizeRequests().antMatchers("/actuator/**").authenticated()
//                .and()
//                .httpBasic();
//                .authorizeRequests()
//                .antMatchers("/actuator/**").permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailService)
                .passwordEncoder(passwordEncoder());
    }


    //不定义没有password grant_type
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    public static class AESEncryptPasswordEncoder implements PasswordEncoder {
        private final TextEncryptor textEncryptor = Encryptors.text("lzu", "deadbeef");

        @Override
        public String encode(CharSequence charSequence) {
            return (String) charSequence;
        }

        @Override
        public boolean matches(CharSequence charSequence, String s) {
            //密码对比 密码对 true 反之 false
            //CharSequence 数据库中的密码
            //s 前台传入的密码
            try {
                return textEncryptor.decrypt(charSequence.toString()).equals(textEncryptor.decrypt(s));
            } catch (Exception e) {
                return false;
            }
        }
    }
}
