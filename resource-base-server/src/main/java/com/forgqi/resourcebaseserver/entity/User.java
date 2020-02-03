package com.forgqi.resourcebaseserver.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;
import java.util.stream.Collectors;


@Entity
@Table(indexes = {@Index(columnList = "user_name", unique = true)})
@Getter @Setter
public class User extends AbstractAuditingEntity implements UserDetails, OAuth2User {
    private static final long serialVersionUID = -1205293048576328829L;

    // 最小值为1可以取到1
    @Min(value = 1, message = "id不能小于0")
    @Id
    private long id;

    @Column(name = "user_name")
    private String userName;
    @JsonIgnore
    @NotNull
    @Column(nullable = false)
    private String password;
    private String nickName;
    private String avatar;

    private String name;
    private String college;
    private String subject;
    private String education;
    private Type type = Type.STUDENT;

    private String grade;
    private String classNo;
    @JsonIgnore
    private String idCard;

    @Size(max = 50)
    private String signature;

    @ColumnDefault("1")
    private boolean accountNonLocked = true;

    @JsonIgnore
    //级联更新，急加载 会查询role表
    @ManyToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @NotAudited
    private List<SysRole> roles = Collections.emptyList();

    @Transient
    private Map<String, Object> attributes;

    public User() {
    }

    User(long id, String password, String nickName) {
        this.id = id;
        this.nickName = nickName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    @JsonIgnore
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles().stream()
                .map(sysRole -> new SimpleGrantedAuthority(sysRole.getRole()))
                .collect(Collectors.toSet());
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return String.valueOf(this.id);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public enum Type {
        STUDENT,
        GRADUATE;
    }
}
