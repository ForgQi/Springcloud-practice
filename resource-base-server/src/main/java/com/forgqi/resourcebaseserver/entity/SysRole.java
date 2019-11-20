package com.forgqi.resourcebaseserver.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

@Entity
public class SysRole implements Serializable {
    private static final long serialVersionUID = 6390950377189896429L;
    @Id
    @GeneratedValue
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
