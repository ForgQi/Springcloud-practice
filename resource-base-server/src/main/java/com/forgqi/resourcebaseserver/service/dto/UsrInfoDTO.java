package com.forgqi.resourcebaseserver.service.dto;

import com.forgqi.resourcebaseserver.entity.User;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
@Builder
public class UsrInfoDTO {
    private Long id;
    private String name;
    private String college;
    private String subject;
    private String education;
    private String grade;
    private String classNo;
    private String idCard;

    public User convertToUser() {
        User user = new User();
        BeanUtils.copyProperties(this, user);
        return user;
    }
}
