package com.forgqi.resourcebaseserver.controller;

import com.forgqi.resourcebaseserver.entity.User;
import com.forgqi.resourcebaseserver.service.UserService;
import com.forgqi.resourcebaseserver.service.dto.UsrPswDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping
public class RegistryController {

    private final UserService userService;

    public RegistryController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/registry")
    public User register(@RequestBody UsrPswDTO usrPswDTO, String type) throws IOException {
        //        UserDTO userDTO = new UserDTO().convertFor(user);
//        userDTO.setTokenDTO(authorizationFeignClient.getToken(loginDTO.convertToTokenMap()));

        return userService.registerUser(usrPswDTO, type);
    }
}
