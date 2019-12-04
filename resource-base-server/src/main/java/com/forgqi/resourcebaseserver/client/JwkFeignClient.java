package com.forgqi.resourcebaseserver.client;

import feign.RequestInterceptor;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;


@FeignClient(name = "JwkFeignClient", url = "http://jwk.lzu.edu.cn/academic", configuration = JwkFeignClient.JwkFeignConfig.class)
public interface JwkFeignClient {
    @GetMapping(value = "/calogin.jsp")
    Response login();

    @GetMapping(value = "/showPersonalInfo.do")
    Response getStuInfo();

    @GetMapping(value = "/student/currcourse/currcourse.jsdo")
    Response getCurrCourse();

    class JwkFeignConfig {
        @Bean
        RequestInterceptor userAgentInterceptor() {
            return (template) -> template.header("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1")
                    .header("Accept", "text/html;charset=UTF-8");
        }
    }
}
