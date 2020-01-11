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

@FeignClient(name = "GmsFeignClient", url = "http://gms.lzu.edu.cn/graduate", configuration = Config.Http2ClientConfig.class)
public interface GmsFeignClient {
    @GetMapping(value = "/ssoLogin.do")
    Response login();

    @GetMapping(value = "/studentinfo/infoView.do")
    Response getStuInfo();

    @GetMapping(value = "/studentschedule/showStudentSchedule.do")
    Response getCurrCourse();
}
