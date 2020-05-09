package com.forgqi.resourcebaseserver.client;

import com.forgqi.resourcebaseserver.client.parse.Config;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "GmsFeignClient", url = "http://gms.lzu.edu.cn/graduate", configuration = Config.Http2ClientConfig.class)
public interface GmsFeignClient {
    @GetMapping(value = "/ssoLogin.do")
    Response login();

    @GetMapping(value = "/studentinfo/infoView.do")
    Response getStuInfo();

    @GetMapping(value = "/studentschedule/showStudentSchedule.do")
    Response getCurrCourse();
}
