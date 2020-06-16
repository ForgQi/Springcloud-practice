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
        User.Detail detail = new User.Detail();
        BeanUtils.copyProperties(this, user);
        BeanUtils.copyProperties(this, detail);
        user.setDetail(detail);
        return user;
    }
}
