package com.forgqi.resourcebaseserver.config;

import com.forgqi.resourcebaseserver.common.FileHandleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

//@Configuration
//public class WebMvcConfiguration implements WebMvcConfigurer {
//
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/upload/**")
//                .addResourceLocations("file:"
//                        + Paths.get(FileHandleUtil.absolutePath,FileHandleUtil.staticDir,"upload")
//                        +System.getProperty("file.separator"));
//    }
//}