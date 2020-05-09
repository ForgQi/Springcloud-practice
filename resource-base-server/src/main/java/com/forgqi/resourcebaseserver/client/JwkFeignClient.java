package com.forgqi.resourcebaseserver.client;

import com.forgqi.resourcebaseserver.client.parse.Config;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;


@FeignClient(name = "JwkFeignClient", url = "http://jwk.lzu.edu.cn/academic", configuration = Config.Http2ClientConfig.class)
public interface JwkFeignClient {
    @GetMapping(value = "/calogin.jsp")
    Response login();

    @GetMapping(value = "/showPersonalInfo.do")
    Response getStuInfo();

    @GetMapping(value = "/student/currcourse/currcourse.jsdo")
    Response getCurrCourse();
}
