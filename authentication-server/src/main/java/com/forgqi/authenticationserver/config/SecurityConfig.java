package com.forgqi.authenticationserver.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgqi.authenticationserver.common.PasswordWriterFilter;
import com.forgqi.authenticationserver.entity.dto.JsonResult;
import com.forgqi.authenticationserver.service.CustomUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import java.io.PrintWriter;
import java.util.UUID;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
//@EnableWebSecurity 没发现作用
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomUserDetailService userDetailService;
    private final ObjectMapper objectMapper;

    public SecurityConfig(CustomUserDetailService userDetailService, ObjectMapper objectMapper) {
        this.userDetailService = userDetailService;
        this.objectMapper = objectMapper;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        //return new BCryptPasswordEncoder();
        return new AESEncryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        http.cors(withDefaults());
        http
                .addFilterBefore(new PasswordWriterFilter(), UsernamePasswordAuthenticationFilter.class)
                .rememberMe().key(UUID.randomUUID().toString())
                .and()
                .formLogin(form -> form
                        //因为表单验证方式默认是跳转页面，而我们前后分离不需要后端处理跳转
                        //所以自定义一个登录成功处理器，它只需要告诉我们登录结果就可以了
                        .successHandler(successHandler())//登录成功处理器
                        .failureHandler(failureHandler())//登录失败处理器
                        .loginPage("/oauth/login"));
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

    private AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> {
            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            JsonResult ok = JsonResult.ok("登录成功");
            out.write(objectMapper.writeValueAsString(ok));
            out.flush();
            out.close();
        };
    }

    /**
     * 自定义登录失败处理器，成功返回一个带有失败信息的Json数据包装类
     */
    private AuthenticationFailureHandler failureHandler() {
        return (request, response, authentication) -> {
            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            JsonResult error = JsonResult.error("账号或密码错误");
            out.write(objectMapper.writeValueAsString(error));
            out.flush();
            out.close();
        };
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
