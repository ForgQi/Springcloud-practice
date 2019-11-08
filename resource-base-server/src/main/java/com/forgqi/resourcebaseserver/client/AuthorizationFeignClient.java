package com.forgqi.resourcebaseserver.client;

import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "auth-server")
public interface AuthorizationFeignClient {
    @PostMapping(value = "/oauth/token")
    Response getToken();
}
