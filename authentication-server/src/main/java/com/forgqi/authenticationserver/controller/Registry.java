package com.forgqi.authenticationserver.controller;

import com.forgqi.authenticationserver.entity.OauthClientDetails;
import com.forgqi.authenticationserver.entity.SysRole;
import com.forgqi.authenticationserver.entity.User;
import com.forgqi.authenticationserver.repository.OauthClientDetailsRepository;
import com.forgqi.authenticationserver.repository.SysRoleRepository;
import com.forgqi.authenticationserver.repository.UserRepository;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/oauth")
@Validated
public class Registry {
    private final
    UserRepository userRepository;
    private final
    SysRoleRepository sysRoleRepository;
    private final
    OauthClientDetailsRepository oauthClientDetailsRepository;

    public Registry(UserRepository userRepository, SysRoleRepository sysRoleRepository, OauthClientDetailsRepository oauthClientDetailsRepository) {
        this.userRepository = userRepository;
        this.sysRoleRepository = sysRoleRepository;
        this.oauthClientDetailsRepository = oauthClientDetailsRepository;
    }

    @RequestMapping(value = "/registry", method = RequestMethod.POST)
    public User register(@RequestBody User user) {
        user.setRoles(null);
        return userRepository.save(user);
    }

    @RequestMapping(value = "/roles", method = RequestMethod.POST)
    public SysRole roles(@NotNull String role) {
        SysRole sysRole = sysRoleRepository.findFirstByRole(role);
        if (sysRole != null) {
            return sysRole;
        }
        return sysRoleRepository.save(new SysRole(role));
    }

    @RequestMapping(value = "/clients", method = RequestMethod.POST)
    public OauthClientDetails clients(OauthClientDetails oauthClientDetails) {
        return oauthClientDetailsRepository.save(oauthClientDetails);
    }

    //        UserHelper.reloadUserFromSecurityContext(principal -> principal.setRoles(roles.stream()
//                .map(role -> new com.forgqi.authenticationserver.entity.SysRole(role.getId(), role.getRole()))
//                .collect(Collectors.toList())), id);
//重新加载redis里存储的token对应用户信息的两个方法
    //            UserHelper.reloadUserFromSecurityContext(user1 -> BeanUtils.copyProperties(user, user1, "roles"));

}
