package com.forgqi.resourcebaseserver.service.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.forgqi.resourcebaseserver.entity.SysRole;
import com.forgqi.resourcebaseserver.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public interface IUserDTO {
    boolean isAccountNonExpired();

    boolean isCredentialsNonExpired();

    boolean isEnabled();

    default Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles().stream()
                .map(sysRole -> new SimpleGrantedAuthority(sysRole.getRole()))
                .collect(Collectors.toSet());
    }

    Long getId();

    String getUserName();

    String getNickName();

    User.Type getType();

    String getName();


    Instant getCreatedDate();

    String getSignature();

    boolean isAccountNonLocked();

    //    @JsonIgnore
    List<SysRole> getRoles();

    String getAvatar();

    @JsonUnwrapped
    Detail getDetail();

    interface Detail {
        String getCollege();

        String getSubject();

        String getEducation();

        String getGrade();

        String getClassNo();

        String getIdCard();
    }
}
