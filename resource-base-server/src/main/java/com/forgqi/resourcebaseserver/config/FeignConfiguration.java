package com.forgqi.resourcebaseserver.config;

import feign.Logger;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.http.HttpClient;

@Configuration
//默认扫描注解所在目录所以需要显式指出basePackages
@EnableFeignClients(basePackages = "com.forgqi.resourcebaseserver.client")
public class FeignConfiguration {
    /**
     * 传入的不是真实对象而是其代理，为了解决注入单例的问题
     * INTERFACES,代理接口
     * TARGET_CLASS,代理类
     * 此版本不会导致其他client也使用此client
     */
    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public HttpClient request() {
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        return HttpClient.newBuilder()
//                .connectTimeout(Duration.ofMillis(30000))
//                .executor(new ContextAwarePoolExecutor())
//                .executor(Executors.newSingleThreadExecutor())
                .cookieHandler(cookieManager)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }

    @Bean
    public Logger.Level logger() {
        return Logger.Level.NONE;
    }
}
