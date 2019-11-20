package com.forgqi.resourcebaseserver.controller;

import com.forgqi.resourcebaseserver.dto.LoginDTO;
import com.forgqi.resourcebaseserver.entity.User;
import com.forgqi.resourcebaseserver.service.client.GmsService;
import com.forgqi.resourcebaseserver.service.client.JwkService;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class RegistryController {

    private final JwkService jwkService;
    private final GmsService gmsService;

    public RegistryController(JwkService jwkService, GmsService gmsService) {
        this.jwkService = jwkService;
        this.gmsService = gmsService;
    }

    @PostMapping(value = "/registry")
    public User register(@RequestBody LoginDTO loginDTO, String type) throws ParseException {
        //        UserDTO userDTO = new UserDTO().convertFor(user);
//        userDTO.setTokenDTO(authorizationFeignClient.getToken(loginDTO.convertToTokenMap()));
        if ("graduate".equals(type)){
            return gmsService.saveStuInfo(loginDTO);
        }
        return jwkService.saveStuInfo(loginDTO);

    }
}
