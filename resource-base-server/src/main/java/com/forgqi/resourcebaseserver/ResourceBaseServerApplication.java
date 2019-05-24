package com.forgqi.resourcebaseserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// 早期的版本（Dalston及更早版本）还需在启动类上添加注解@EnableDiscoveryClient或@EnableEurekaClient ，
// 从Edgware开始，该注解可省略。
@SpringBootApplication
public class ResourceBaseServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResourceBaseServerApplication.class, args);
    }

}
