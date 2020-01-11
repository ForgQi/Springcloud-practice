package com.forgqi.resourcebaseserver.client;

import com.forgqi.resourcebaseserver.client.parse.Config;
import feign.Client;
import feign.RequestInterceptor;
import feign.Response;
import feign.http2client.Http2Client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.http.HttpClient;


@FeignClient(name = "JwkFeignClient", url = "http://jwk.lzu.edu.cn/academic", configuration = Config.Http2ClientConfig.class)
public interface JwkFeignClient {
    @GetMapping(value = "/calogin.jsp")
    Response login();

    @GetMapping(value = "/showPersonalInfo.do")
    Response getStuInfo();

    @GetMapping(value = "/student/currcourse/currcourse.jsdo")
    Response getCurrCourse();
}
