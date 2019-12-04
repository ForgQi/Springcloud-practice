package com.forgqi.resourcebaseserver.service;

import com.forgqi.resourcebaseserver.common.util.UserHelper;
import com.forgqi.resourcebaseserver.entity.User;
import com.forgqi.resourcebaseserver.repository.SysRoleRepository;
import com.forgqi.resourcebaseserver.repository.UserRepository;
import com.forgqi.resourcebaseserver.service.client.GmsService;
import com.forgqi.resourcebaseserver.service.client.JwkService;
import com.forgqi.resourcebaseserver.service.dto.Editable;
import com.forgqi.resourcebaseserver.service.dto.UsrPswDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final
    UserRepository userRepository;
    private final
    SysRoleRepository sysRoleRepository;
    private final GmsService gmsService;
    private final JwkService jwkService;

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, SysRoleRepository sysRoleRepository, @Qualifier("defaultTokenServices") ResourceServerTokenServices tokenServices, GmsService gmsService, JwkService jwkService) {
        this.userRepository = userRepository;
        this.sysRoleRepository = sysRoleRepository;
        this.gmsService = gmsService;
        this.jwkService = jwkService;
    }

    @Transactional
    public User registerUser(UsrPswDTO usrPswDTO, String type) throws IOException {

        User user;
        if ("graduate".equals(type)) {
            user = gmsService.saveStuInfo(usrPswDTO);
        } else {
            user = jwkService.saveStuInfo(usrPswDTO);
        }
        userRepository.findByUserName(usrPswDTO.getUserName()).ifPresent(u -> {
            user.setCreatedDate(u.getCreatedDate());
            user.setAvatar(u.getAvatar());
            user.setNickName(u.getNickName());
        });
        user.setUserName(usrPswDTO.getUserName());
        user.setPassword(usrPswDTO.getPassword());
        return userRepository.save(user);

    }

    public Optional<User> findUserBySecurityContextFormRepository() {
        return UserHelper.getUserBySecurityContext()
                .flatMap(user -> userRepository.findById(user.getId()));
    }

    public User reloadUserFromSecurityContext(Long id, List<String> sysRoles) {

        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("用户不存在"));

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
            if (editable.getAvatar() != null) {
                user.setAvatar(editable.getAvatar());
            }
            if (editable.getNickname() != null) {
                user.setNickName(editable.getNickname());
            }
            UserHelper.reloadUserFromSecurityContext(user1 -> {
                user1.setAvatar(user.getAvatar());
                user1.setNickName(user.getNickName());
            });
            return userRepository.save(user);
        });
    }
}
