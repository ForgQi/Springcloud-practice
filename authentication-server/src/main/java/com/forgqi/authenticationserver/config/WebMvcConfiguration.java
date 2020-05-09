package com.forgqi.authenticationserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/oauth/login").setViewName("login");
    }
}

@Configuration
@Profile({"dev"})
class CorsConfig implements WebMvcConfigurer {
    //    这种方法配置之后再使用自定义拦截器时跨域相关配置就会失效。
//    原因是请求经过的先后顺序问题，当请求到来时会先进入拦截器中，
//    而不是进入Mapping映射中，所以返回的头信息中并没有配置的跨域信息。
//    浏览器就会报跨域异常。所以需要在security中配置cors()
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 设置允许跨域的路径
        registry.addMapping("/**")
                // 设置允许跨域请求的域名
                .allowedOrigins("*")
                // 是否允许证书
                .allowCredentials(true)
                // 设置允许的方法
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                // 设置允许的header属性
                .allowedHeaders("*")
                // 跨域允许时间
                .maxAge(3600);
    }
}
