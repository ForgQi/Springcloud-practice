package com.forgqi.resourcebaseserver.service.client;

import com.forgqi.resourcebaseserver.client.ZhxgFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URI;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ZhxgService {
    private final CookieManager cookieManager;

    public List<HttpCookie> getCookie() {
        return cookieManager.getCookieStore().get(URI.create("http://zhxg.lzu.edu.cn/"));
//        return cookieManager.getCookieStore().get(URI.create("http://zhxg.lzu.edu.cn/lzuyz/sys/sysindex/toIndex"))
//                .stream().filter(httpCookie -> "JSESSIONID".equals(httpCookie.getName()))
//                .findFirst()
//                .map(HttpCookie::getValue).get();
    }
}
