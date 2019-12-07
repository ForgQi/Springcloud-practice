package com.forgqi.resourcebaseserver.service.dto;


import com.forgqi.resourcebaseserver.entity.SysRole;
import com.forgqi.resourcebaseserver.entity.User;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.time.Instant;
import java.util.List;

@Data
public class UserDTO {
    private Long id;
    private String name;
    private String college;
    private String subject;
    private String education;
    private String grade;
    private String classNo;
    private Instant createdDate;
    private TokenDTO tokenDTO;
    private List<SysRole> roles;
    private String nickname;
    private String avatar;

    public UserDTO convertFor(User user) {
        BeanUtils.copyProperties(user, this);
        return this;
    }
}
