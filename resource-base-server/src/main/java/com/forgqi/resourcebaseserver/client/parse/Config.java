package com.forgqi.resourcebaseserver.client.parse;

import feign.Client;
import feign.RequestInterceptor;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import feign.http2client.Http2Client;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.net.http.HttpClient;

public interface Config {

    @Import({Http2ClientConfig.class, FormEncoderConfig.class})
    class CombinatorialConfig {
    }

    class FormEncoderConfig {
//        @Autowired 不需要此注解会注入
//        private ObjectFactory<HttpMessageConverters> messageConverters;

        // new一个form编码器，实现支持form表单提交
        @Bean
        public Encoder feignFormEncoder(ObjectFactory<HttpMessageConverters> messageConverters) {
            return new SpringFormEncoder(new SpringEncoder(messageConverters));
        }
    }

    class Http2ClientConfig {

        @Bean
        RequestInterceptor userAgentInterceptor() {
            return (template) -> template.header("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1")
                    .header("Accept", "*/*;charset=UTF-8");
        }

        @Bean
        public Client client(HttpClient httpClient) {
            return new Http2Client(httpClient);
        }
    }
}
