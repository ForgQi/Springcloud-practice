package com.forgqi.resourcebaseserver.client;

import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "ZhxgFeignClient", url = "http://zhxg.lzu.edu.cn")
public interface ZhxgFeignClient {
    @GetMapping("/lzuyz/sys/sysuser/loginPortal")
    Response login();
}
