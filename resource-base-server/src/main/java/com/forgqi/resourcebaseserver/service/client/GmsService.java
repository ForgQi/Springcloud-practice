package com.forgqi.resourcebaseserver.service.client;

import com.forgqi.resourcebaseserver.client.parse.GmsParse;
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

import com.forgqi.authenticationserver.entity.User.Type;

@Service
public class GmsService {
    private final GmsParse gmsParse;
    private final UserRepository userRepository;
    private final CookieManager cookieManager;


    public GmsService(GmsParse gmsParse, UserRepository userRepository, CookieManager cookieManager) {
        this.gmsParse = gmsParse;
        this.userRepository = userRepository;
        this.cookieManager = cookieManager;
    }

    @Transactional
    public User saveStuInfo(LoginDTO loginDTO) throws ParseException {
        try {
            StuInfoDTO stuInfoDTO = gmsParse.getStuInfo();
            User user = stuInfoDTO.convertToUser(loginDTO);
            userRepository.findByUserName(loginDTO.getUserName()).ifPresent(u -> user.setCreatedDate(u.getCreatedDate()));
            user.setType(Type.GRADUATE);
            return userRepository.save(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ParseException("用户信息解析错误");
        }
    }

    public List<HttpCookie> getCookie() {
        return cookieManager.getCookieStore().get(URI.create("http://gms.lzu.edu.cn/"));
    }
    public List<CourseDTO> getCourse() throws IOException {
        return gmsParse.getCourse();
    }
}
