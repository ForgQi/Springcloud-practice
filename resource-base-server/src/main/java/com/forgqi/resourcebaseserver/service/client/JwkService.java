package com.forgqi.resourcebaseserver.service.client;

import com.forgqi.resourcebaseserver.client.JwkFeignClient;
import com.forgqi.resourcebaseserver.client.parse.JwkParse;
import com.forgqi.resourcebaseserver.entity.User;
import com.forgqi.resourcebaseserver.repository.UserRepository;
import com.forgqi.resourcebaseserver.service.dto.CourseDTO;
import com.forgqi.resourcebaseserver.service.dto.UsrPswDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URI;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JwkService {
    private final JwkParse jwkParse;
    private final CookieManager cookieManager;

    /**
     * @return 用户信息
     */
    @Transactional
    public User saveStuInfo() {
        return jwkParse.getStuInfo();
    }

    public List<HttpCookie> getCookie() {
        return cookieManager.getCookieStore().get(URI.create("http://jwk.lzu.edu.cn/"));
//        return cookieManager.getCookieStore().get(URI.create("http://jwk.lzu.edu.cn/"))
//                .stream().filter(httpCookie -> "JSESSIONID".equals(httpCookie.getName()))
//                .findFirst()
//                .map(HttpCookie::getValue).get();
    }

    public List<CourseDTO> getCourse() throws IOException {
        return jwkParse.getCourse();
    }
}
