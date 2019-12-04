package com.forgqi.resourcebaseserver.service.client;

import com.forgqi.resourcebaseserver.client.JwkFeignClient;
import com.forgqi.resourcebaseserver.client.parse.JwkParse;
import com.forgqi.resourcebaseserver.entity.User;
import com.forgqi.resourcebaseserver.repository.UserRepository;
import com.forgqi.resourcebaseserver.service.dto.CourseDTO;
import com.forgqi.resourcebaseserver.service.dto.UsrPswDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URI;
import java.util.List;

@Service
public class JwkService {
    private final UserRepository userRepository;
    private final JwkParse jwkParse;
    private final JwkFeignClient jwkFeignClient;
    private final CookieManager cookieManager;


    public JwkService(UserRepository userRepository, JwkParse jwkParse, JwkFeignClient jwkFeignClient, CookieManager cookieManager) {
        this.userRepository = userRepository;
        this.jwkParse = jwkParse;
        this.jwkFeignClient = jwkFeignClient;
        this.cookieManager = cookieManager;
    }

    /**
     * 参数不能省，给aop登录使用
     *
     * @param usrPswDTO 用户名密码
     * @return 用户信息
     * @throws IOException 解析可能出错
     */
    @Transactional
    public User saveStuInfo(UsrPswDTO usrPswDTO) throws IOException {
        return jwkParse.getStuInfo();
//            userRepository.findByUserName(usrPswDTO.getUserName()).ifPresent(u -> {
//                user.setCreatedDate(u.getCreatedDate());
//                user.setUserName(usrPswDTO.getUserName());
//                user.setPassword(usrPswDTO.getPassword());
//            });
//            return userRepository.save(user);
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
