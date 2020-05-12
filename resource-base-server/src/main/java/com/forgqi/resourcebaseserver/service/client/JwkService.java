package com.forgqi.resourcebaseserver.service.client;

import com.forgqi.resourcebaseserver.client.parse.JwkParse;
import com.forgqi.resourcebaseserver.entity.User;
import com.forgqi.resourcebaseserver.service.dto.CourseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URI;
import java.net.http.HttpClient;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JwkService {
    private final JwkParse jwkParse;
    private final HttpClient httpClient;

    /**
     * @return 用户信息
     */
    @Transactional
    public User saveStuInfo() {
        return jwkParse.getStuInfo();
    }

    public List<HttpCookie> getCookie() {
        return ((CookieManager) httpClient.cookieHandler().orElseThrow())
                .getCookieStore().get(URI.create("http://jwk.lzu.edu.cn/"));
//        return cookieManager.getCookieStore().get(URI.create("http://jwk.lzu.edu.cn/"))
//                .stream().filter(httpCookie -> "JSESSIONID".equals(httpCookie.getName()))
//                .findFirst()
//                .map(HttpCookie::getValue).get();
    }

    public List<CourseDTO> getCourse() {
        return jwkParse.getCourse();
    }
}
