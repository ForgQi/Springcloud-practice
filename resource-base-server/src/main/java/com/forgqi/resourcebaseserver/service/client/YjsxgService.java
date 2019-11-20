package com.forgqi.resourcebaseserver.service.client;

import org.springframework.stereotype.Service;

import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URI;
import java.util.List;

@Service
public class YjsxgService {
    private final CookieManager cookieManager;

    public YjsxgService(CookieManager cookieManager) {
        this.cookieManager = cookieManager;
    }

    public List<HttpCookie> getCookie() {
        return cookieManager.getCookieStore().get(URI.create("http://zhxg.lzu.edu.cn/"));
    }
}
