package com.forgqi.resourcebaseserver.client;

import feign.RequestInterceptor;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "GmsFeignClient", url = "http://gms.lzu.edu.cn/graduate", configuration = GmsFeignClient.Config.class)
public interface GmsFeignClient {
    @GetMapping(value = "/ssoLogin.do")
    Response login();

    @GetMapping(value = "/studentinfo/infoView.do")
    Response getStuInfo();

    @GetMapping(value = "/studentschedule/showStudentSchedule.do")
    Response getCurrCourse();

    class Config {

        @Bean
        RequestInterceptor userAgentInterceptor() {
            return (template) -> template.header("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1")
                    .header("Accept", "text/html;charset=UTF-8");
        }
    }
}
