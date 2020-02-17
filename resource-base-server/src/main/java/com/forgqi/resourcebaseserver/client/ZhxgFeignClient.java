package com.forgqi.resourcebaseserver.client;

import com.forgqi.resourcebaseserver.client.parse.Config;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "ZhxgFeignClient", url = "http://zhxg.lzu.edu.cn", configuration = Config.Http2ClientConfig.class)
public interface ZhxgFeignClient {
    @GetMapping("/lzuyz/sys/sysuser/loginPortal")
    Response login();
}
