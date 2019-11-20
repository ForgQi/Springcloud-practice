package com.forgqi.resourcebaseserver.controller;

import com.forgqi.resourcebaseserver.dto.Editable;
import com.forgqi.resourcebaseserver.entity.User;
import com.forgqi.resourcebaseserver.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping(value = "/editable")
    public Optional<User> change(Editable editable){
        return userService.changeAvatarOrNickName(editable);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.PUT)
//    public String home(@RequestParam(value = "name", defaultValue = "World") String name)
    public User role(@PathVariable Long id, @RequestBody List<String> sysRoles) {
//        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
//        String name = userDetails.getUsername();
        return userService.reloadUserFromSecurityContext(id, sysRoles);
    }

}
