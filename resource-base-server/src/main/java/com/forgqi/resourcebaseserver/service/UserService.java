package com.forgqi.resourcebaseserver.service;

import com.forgqi.resourcebaseserver.common.UserHelper;
import com.forgqi.resourcebaseserver.dto.Editable;
import com.forgqi.resourcebaseserver.entity.SysRole;
import com.forgqi.resourcebaseserver.entity.User;
import com.forgqi.resourcebaseserver.repository.SysRoleRepository;
import com.forgqi.resourcebaseserver.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final
    UserRepository userRepository;
    private final
    SysRoleRepository sysRoleRepository;

    public UserService(UserRepository userRepository, SysRoleRepository sysRoleRepository, @Qualifier("defaultTokenServices") ResourceServerTokenServices tokenServices) {
        this.userRepository = userRepository;
        this.sysRoleRepository = sysRoleRepository;
    }
    public User reloadUserFromSecurityContext(Long id, List<String> sysRoles) {

        User user = userRepository.findById(id).orElseThrow(()->new UsernameNotFoundException("用户不存在"));

        UserHelper.reloadUserFromSecurityContext(principal -> {
            principal.setRoles(sysRoles.stream()
                    .map(sysRoleRepository::findFirstByRole)
                    .map(role -> {
                        com.forgqi.authenticationserver.entity.SysRole sysRole = new com.forgqi.authenticationserver.entity.SysRole(role.getRole());
                        sysRole.setId(role.getId());
                        return sysRole;
                    }).collect(Collectors.toList()));
        }, id);
        return userRepository.save(user);
    }
    public Optional<User> changeAvatarOrNickName(Editable editable) {

        return UserHelper.getUserBySecurityContext().map(user -> {
            if (editable.getAvatar() != null){
                user.setAvatar(editable.getAvatar());
            }
            if (editable.getNickname() != null){
                user.setNickName(editable.getNickname());
            }
            UserHelper.reloadUserFromSecurityContext(user1 ->{
                user1.setAvatar(user.getAvatar());
                user1.setNickName(user.getNickName());
            });
            return userRepository.save(user);
        });
    }
}
