package com.forgqi.resourcebaseserver.service.client;

import com.forgqi.resourcebaseserver.client.parse.GmsParse;
import com.forgqi.resourcebaseserver.entity.User;
import com.forgqi.resourcebaseserver.service.dto.CourseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URI;
import java.net.http.HttpClient;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GmsService {
    private final GmsParse gmsParse;
    private final HttpClient httpClient;

    /**
     * @return 用户信息
     */
    @Transactional
    public User saveStuInfo(){
        User user = gmsParse.getStuInfo();
//            userRepository.findByUserName(usrPswDTO.getUserName()).ifPresent(u -> user.setCreatedDate(u.getCreatedDate()));
        user.setType(User.Type.GRADUATE);
        return user;
    }

    public List<HttpCookie> getCookie() {
        return ((CookieManager)httpClient.cookieHandler().orElseThrow())
                .getCookieStore().get(URI.create("http://gms.lzu.edu.cn/"));
    }

    public List<CourseDTO> getCourse() throws IOException {
        return gmsParse.getCourse();
    }
}
