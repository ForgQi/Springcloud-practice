package com.forgqi.resourcebaseserver.config;

import feign.Client;
import feign.Logger;
import feign.http2client.Http2Client;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.http.HttpClient;
@Configuration
//默认扫描注解所在目录所以需要显式指出basePackages
@EnableFeignClients(basePackages = "com.forgqi.resourcebaseserver.client")
public class FeignConfiguration {
    @Bean
    public CookieManager cookieManager(){
        return new CookieManager();
    }
    @Bean
//    @Scope()
    public Client client() {
        CookieManager cookieManager = cookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        return new Http2Client(HttpClient.newBuilder()
                .cookieHandler(cookieManager)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build());
    }

    @Bean
    public Logger.Level logger() {
        return Logger.Level.FULL;
    }
}
