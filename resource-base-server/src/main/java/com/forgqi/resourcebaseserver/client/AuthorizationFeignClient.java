package com.forgqi.resourcebaseserver.client;

import com.forgqi.resourcebaseserver.client.parse.Config;
import com.forgqi.resourcebaseserver.service.dto.TokenDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@FeignClient(name = "auth-server", url = "http://localhost:8081", configuration = Config.FormEncoderConfig.class)
public interface AuthorizationFeignClient {
    @PostMapping(value = "/oauth/token", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    TokenDTO getToken(Map<String, ?> info);
}
