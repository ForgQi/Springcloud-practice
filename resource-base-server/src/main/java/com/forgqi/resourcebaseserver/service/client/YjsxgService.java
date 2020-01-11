package com.forgqi.resourcebaseserver.service.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URI;
import java.net.http.HttpClient;
import java.util.List;

@Service
@RequiredArgsConstructor
public class YjsxgService {
    private final HttpClient httpClient;

    public List<HttpCookie> getCookie() {
        return ((CookieManager)httpClient.cookieHandler().orElseThrow())
                .getCookieStore().get(URI.create("http://zhxg.lzu.edu.cn/"));
    }
}
