package com.forgqi.resourcebaseserver.service;

import com.forgqi.resourcebaseserver.common.util.ParseUtil;
import com.forgqi.resourcebaseserver.common.util.UserHelper;
import com.forgqi.resourcebaseserver.entity.SysRole;
import com.forgqi.resourcebaseserver.entity.User;
import com.forgqi.resourcebaseserver.repository.SysRoleRepository;
import com.forgqi.resourcebaseserver.repository.UserRepository;
import com.forgqi.resourcebaseserver.service.client.GmsService;
import com.forgqi.resourcebaseserver.service.client.JwkService;
import com.forgqi.resourcebaseserver.service.dto.Editable;
import com.forgqi.resourcebaseserver.service.dto.UsrPswDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
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

//    private final Logger log = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, SysRoleRepository sysRoleRepository, GmsService gmsService, JwkService jwkService) {
        this.userRepository = userRepository;
        this.sysRoleRepository = sysRoleRepository;
        this.gmsService = gmsService;
        this.jwkService = jwkService;
    }

    public User registerUser(UsrPswDTO usrPswDTO, String type){
        User user;
        if ("graduate".equals(type)) {
            user = gmsService.saveStuInfo();
        } else {
            user = jwkService.saveStuInfo();
        }
        User temporaryUser = new User();
        userRepository.findByUserName(usrPswDTO.getUserName()).ifPresent(u -> BeanUtils.copyProperties(u, temporaryUser));
        String[] nullPropertyNames = ParseUtil.getNullPropertyNames(user);
        String[] copyOf = Arrays.copyOf(nullPropertyNames, nullPropertyNames.length + 2);
        copyOf[nullPropertyNames.length] = "accountNonLocked";
        copyOf[nullPropertyNames.length + 1] = "roles";
        BeanUtils.copyProperties(user, temporaryUser, copyOf);
        temporaryUser.setUserName(usrPswDTO.getUserName());
        temporaryUser.setPassword(usrPswDTO.getPassword());
        return userRepository.save(temporaryUser);

    }

    public User reloadUserFromSecurityContext(Long id, List<String> sysRoles) {

        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("用户不存在"));
        List<SysRole> roles = sysRoles.stream()
                .map(sysRoleRepository::findFirstByRole)
                .collect(Collectors.toList());

        UserHelper.reloadUserFromSecurityContext(principal -> principal.setRoles(roles.stream()
                .map(role -> new com.forgqi.authenticationserver.entity.SysRole(role.getId(), role.getRole()))
                .collect(Collectors.toList())), id);
        user.setRoles(roles);
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
            if (editable.getIndividualSignature() != null) {
                user.setSignature(editable.getIndividualSignature());
            }
            UserHelper.reloadUserFromSecurityContext(user1 -> BeanUtils.copyProperties(user, user1, "roles"));
            return userRepository.save(user);
        });
    }
}
