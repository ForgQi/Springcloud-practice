package com.forgqi.resourcebaseserver.controller;

import com.forgqi.resourcebaseserver.common.util.UserHelper;
import com.forgqi.resourcebaseserver.entity.User;
import com.forgqi.resourcebaseserver.service.client.GmsService;
import com.forgqi.resourcebaseserver.service.client.JwkService;
import com.forgqi.resourcebaseserver.service.client.YjsxgService;
import com.forgqi.resourcebaseserver.service.client.ZhxgService;
import com.forgqi.resourcebaseserver.service.dto.CourseDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.HttpCookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/v1")
public class MISController {
    private final JwkService jwkService;
    private final GmsService gmsService;
    private final ZhxgService zhxgService;
    private final YjsxgService yjsxgService;

    public MISController(JwkService jwkService, GmsService gmsService, ZhxgService zhxgService, YjsxgService yjsxgService) {
        this.jwkService = jwkService;
        this.gmsService = gmsService;
        this.zhxgService = zhxgService;
        this.yjsxgService = yjsxgService;
    }

    @GetMapping(value = "/course")
    List<CourseDTO> getCourse(Integer user) throws IOException {
        User user1 = UserHelper.getUserBySecurityContext().orElseThrow();
        List<CourseDTO> courseDTOList;
        if (user1.getType() == User.Type.STUDENT) {
            courseDTOList = jwkService.getCourse();
        } else {
            courseDTOList = gmsService.getCourse();
        }
        courseDTOList.forEach(courseDTO -> courseDTO.setUser(user));
        return courseDTOList;
    }

    @GetMapping(value = "/cookie")
    public Map<String, List<HttpCookie>> getCookie() {
        HashMap<String, List<HttpCookie>> map = new HashMap<>();
        User user = UserHelper.getUserBySecurityContext().orElseThrow();
        if (user.getType() == User.Type.STUDENT) {
            map.put("jwk", jwkService.getCookie());
            map.put("zhxg", zhxgService.getCookie());
            return map;
        }
        map.put("gms", gmsService.getCookie());
        map.put("yjsxg", yjsxgService.getCookie());
        return map;
    }
}
