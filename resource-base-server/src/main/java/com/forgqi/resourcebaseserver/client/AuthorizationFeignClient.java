package com.forgqi.resourcebaseserver.client;

import com.forgqi.resourcebaseserver.dto.TokenDTO;
import feign.Response;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@FeignClient(name = "auth-server", url = "http://localhost:8081", configuration = AuthorizationFeignClient.FeignConfig.class)
public interface AuthorizationFeignClient {
    @PostMapping(value = "/oauth/token", consumes = {
            MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    TokenDTO getToken(Map<String, ?> info);

    class FeignConfig {
        @Autowired
        private ObjectFactory<HttpMessageConverters> messageConverters;
        // new一个form编码器，实现支持form表单提交
        @Bean
        public Encoder feignFormEncoder() {
            return new SpringFormEncoder(new SpringEncoder(messageConverters));
        }
    }
}
