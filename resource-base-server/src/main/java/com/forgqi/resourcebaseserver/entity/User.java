package com.forgqi.resourcebaseserver.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.forgqi.authenticationserver.entity.User.Type;

@Entity
@Table(indexes = {@Index(columnList = "user_name", unique = true)})
@Getter
@Setter
public class User extends AbstractAuditingEntity implements UserDetails {
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

}
