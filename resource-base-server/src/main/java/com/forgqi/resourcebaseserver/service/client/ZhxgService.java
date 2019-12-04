package com.forgqi.resourcebaseserver.service.client;

import com.forgqi.resourcebaseserver.client.ZhxgFeignClient;
import org.springframework.stereotype.Service;

import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URI;
import java.util.List;

@Service
public class ZhxgService {
    private final ZhxgFeignClient zhxgFeignClient;
    private final CookieManager cookieManager;

    public ZhxgService(ZhxgFeignClient zhxgFeignClient, CookieManager cookieManager) {
        this.zhxgFeignClient = zhxgFeignClient;
        this.cookieManager = cookieManager;
    }

    public List<HttpCookie> getCookie() {
        return cookieManager.getCookieStore().get(URI.create("http://zhxg.lzu.edu.cn/"));
//        return cookieManager.getCookieStore().get(URI.create("http://zhxg.lzu.edu.cn/lzuyz/sys/sysindex/toIndex"))
//                .stream().filter(httpCookie -> "JSESSIONID".equals(httpCookie.getName()))
//                .findFirst()
//                .map(HttpCookie::getValue).get();
    }
}
