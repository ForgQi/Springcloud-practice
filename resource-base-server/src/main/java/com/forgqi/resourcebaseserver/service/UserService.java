package com.forgqi.resourcebaseserver.service;

import com.forgqi.resourcebaseserver.common.util.ParseUtil;
import com.forgqi.resourcebaseserver.common.util.UserHelper;
import com.forgqi.resourcebaseserver.entity.SysRole;
import com.forgqi.resourcebaseserver.entity.User;
import com.forgqi.resourcebaseserver.repository.SysRoleRepository;
import com.forgqi.resourcebaseserver.repository.UserRepository;
import com.forgqi.resourcebaseserver.service.client.GmsService;
import com.forgqi.resourcebaseserver.service.client.JwkService;
import com.forgqi.resourcebaseserver.service.client.MyLzuService;
import com.forgqi.resourcebaseserver.service.dto.Editable;
import com.forgqi.resourcebaseserver.service.dto.UsrPswDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final SysRoleRepository sysRoleRepository;
    private final GmsService gmsService;
    private final JwkService jwkService;
    private final MyLzuService myLzuService;

//    private final Logger log = LoggerFactory.getLogger(UserService.class);

    public User registerUser(UsrPswDTO usrPswDTO, String type) {
        User user;
        User.Type userType = User.Type.valueOf(type.toUpperCase());
        if (User.Type.GRADUATE == userType) {
            user = gmsService.saveStuInfo();
        } else if (User.Type.STUDENT == userType) {
            user = jwkService.saveStuInfo();
        } else {
            user = myLzuService.getUser();
            user.setType(userType);
        }
//        User temporaryUser = new User();
//        userRepository.findByUserName(usrPswDTO.getUserName()).ifPresent(u -> BeanUtils.copyProperties(u, temporaryUser));
        userRepository.findByUserName(usrPswDTO.getUserName()).ifPresent(u -> BeanUtils.copyProperties(u, user, "detail"));
//        String[] nullPropertyNames = ParseUtil.getNullPropertyNames(user);
//        String[] copyOf = Arrays.copyOf(nullPropertyNames, nullPropertyNames.length + 2);
//        copyOf[nullPropertyNames.length] = "accountNonLocked";
//        copyOf[nullPropertyNames.length + 1] = "roles";
//        BeanUtils.copyProperties(user, temporaryUser, copyOf);
//        temporaryUser.setUserName(usrPswDTO.getUserName());
//        temporaryUser.setPassword(usrPswDTO.getPassword());
        user.setUserName(usrPswDTO.getUserName());
        user.setPassword(usrPswDTO.getPassword());
        return userRepository.save(user);

    }

    public User reloadUserFromSecurityContext(Long id, List<String> sysRoles) {

        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("用户不存在"));
        List<SysRole> roles = sysRoles.stream()
                .map(sysRoleRepository::findFirstByRole)
                .collect(Collectors.toList());

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
            return userRepository.save(user);
        });
    }
}
