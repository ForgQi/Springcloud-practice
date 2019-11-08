package com.forgqi.resourcebaseserver.service.client;

import com.forgqi.resourcebaseserver.client.parse.JwkParse;
import com.forgqi.resourcebaseserver.dto.LoginDTO;
import com.forgqi.resourcebaseserver.dto.StuInfoDTO;
import com.forgqi.resourcebaseserver.entity.User;
import com.forgqi.resourcebaseserver.repository.UserRepository;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.stereotype.Service;

@Service
public class JwkService {
    private final UserRepository userRepository;
    private final JwkParse jwkParse;

    public JwkService(UserRepository userRepository, JwkParse jwkParse) {
        this.userRepository = userRepository;
        this.jwkParse = jwkParse;
    }

    public User saveStuInfo(LoginDTO loginDTO) throws ParseException {
        try {
            StuInfoDTO stuInfoDTO = jwkParse.getStuInfo();
            return userRepository.save(stuInfoDTO.convertToUser(loginDTO));
        } catch (Exception e) {
            e.printStackTrace();
            throw new ParseException("用户信息解析错误");
        }
    }
}
