package com.forgqi.authenticationserver.entity;

import java.io.Serializable;


public class SysRole implements Serializable {
    private static final long serialVersionUID = 6390950377189896429L;

    private Long id;
    private String role;

    SysRole() {
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
