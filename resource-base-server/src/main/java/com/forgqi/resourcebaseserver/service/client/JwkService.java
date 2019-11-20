package com.forgqi.resourcebaseserver.service.client;

import com.forgqi.resourcebaseserver.client.JwkFeignClient;
import com.forgqi.resourcebaseserver.client.parse.JwkParse;
import com.forgqi.resourcebaseserver.dto.CourseDTO;
import com.forgqi.resourcebaseserver.dto.LoginDTO;
import com.forgqi.resourcebaseserver.dto.StuInfoDTO;
import com.forgqi.resourcebaseserver.entity.User;
import com.forgqi.resourcebaseserver.repository.UserRepository;
import org.apache.tomcat.util.json.ParseException;
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
    @Transactional
    public User saveStuInfo(LoginDTO loginDTO) throws ParseException {
        try {
            StuInfoDTO stuInfoDTO = jwkParse.getStuInfo();
            User user = stuInfoDTO.convertToUser(loginDTO);
            userRepository.findByUserName(loginDTO.getUserName()).ifPresent(u -> user.setCreatedDate(u.getCreatedDate()));
            return userRepository.save(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ParseException("用户信息解析错误");
        }
    }
    public List<HttpCookie> getCookie(){
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
