package com.forgqi.authenticationserver.entity;

import java.io.Serializable;


public class SysRole implements Serializable {
    private static final long serialVersionUID = 6390950377189896429L;

    private Long id;
    private String name;

    SysRole() {
    }

    public SysRole(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
