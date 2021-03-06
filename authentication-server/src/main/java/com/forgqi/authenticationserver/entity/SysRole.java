package com.forgqi.authenticationserver.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Entity
public class SysRole implements Serializable {
    private static final long serialVersionUID = 6390950377189896429L;
    @Id
    @GeneratedValue
    private Long id;
    @NotBlank
    private String role;

    public SysRole() {
    }

    public SysRole(String role) {
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
