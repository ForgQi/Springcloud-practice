package com.forgqi.resourcebaseserver.config;

import com.forgqi.resourcebaseserver.common.RegisterInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RegisterInterceptor()).addPathPatterns("/registry");
    }
}