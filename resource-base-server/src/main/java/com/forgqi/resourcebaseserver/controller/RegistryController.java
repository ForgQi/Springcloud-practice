package com.forgqi.resourcebaseserver.controller;

import com.forgqi.resourcebaseserver.dto.LoginDTO;
import com.forgqi.resourcebaseserver.entity.User;
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

    public RegistryController(JwkService jwkService) {
        this.jwkService = jwkService;
    }

    @PostMapping(value = "/registry")
    public User register(@RequestBody LoginDTO loginDTO) throws ParseException {
        return jwkService.saveStuInfo(loginDTO);

        //        user.setRoles(null);
//        return userRepository.save(user);
//        collection
//                .stream()
//                .map(s -> s.split(";")[0])
//                .collect(Collectors.toList());
    }
}
