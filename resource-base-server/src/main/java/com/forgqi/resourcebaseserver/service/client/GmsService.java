package com.forgqi.resourcebaseserver.service.client;

import com.forgqi.authenticationserver.entity.User.Type;
import com.forgqi.resourcebaseserver.client.parse.GmsParse;
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
public class GmsService {
    private final GmsParse gmsParse;
    private final UserRepository userRepository;
    private final CookieManager cookieManager;


    public GmsService(GmsParse gmsParse, UserRepository userRepository, CookieManager cookieManager) {
        this.gmsParse = gmsParse;
        this.userRepository = userRepository;
        this.cookieManager = cookieManager;
    }

    /**
     * 参数不能省给aop登录使用
     *
     * @param usrPswDTO 用户名密码
     * @return 用户信息
     * @throws IOException 解析可能出错
     */
    @Transactional
    public User saveStuInfo(UsrPswDTO usrPswDTO) throws IOException {
        User user = gmsParse.getStuInfo();
//            userRepository.findByUserName(usrPswDTO.getUserName()).ifPresent(u -> user.setCreatedDate(u.getCreatedDate()));
        user.setType(Type.GRADUATE);
        return user;
    }

    public List<HttpCookie> getCookie() {
        return cookieManager.getCookieStore().get(URI.create("http://gms.lzu.edu.cn/"));
    }

    public List<CourseDTO> getCourse() throws IOException {
        return gmsParse.getCourse();
    }
}
