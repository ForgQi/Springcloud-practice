package com.forgqi.resourcebaseserver.client;

import feign.Logger;
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
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

// 在使用url属性时，在老版本的Spring Cloud中，不需要提供name属性，
// 但是在新版本（例如Brixton、Camden）@FeignClient必须提供name属性，
// 并且name、url属性支持占位符。
@FeignClient(name = "TestFeignClient", url = "http://my.lzu.edu.cn", configuration = LoginFeignClient.FeignConfig.class)
public interface LoginFeignClient {
    @PostMapping(value = "/userPasswordValidate.portal",
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE},
            headers = {"Referer=http://my.lzu.edu.cn/login.portal"}
    )
    // feign.Headers注解，需要和RequestLine注解一起使用，才可以生效。
    // 如果和RequestMapping注解一起使用，是不生效的
    // @Headers({"Referer: http://my.lzu.edu.cn/login.portal"})
    Response post(Map<String, ?> user);

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