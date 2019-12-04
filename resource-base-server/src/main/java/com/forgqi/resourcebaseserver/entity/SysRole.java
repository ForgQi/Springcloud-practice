package com.forgqi.resourcebaseserver.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysRole implements Serializable {
    private static final long serialVersionUID = 6390950377189896429L;
    @Id
    @GeneratedValue
    private Long id;
    private String role;

    public SysRole(String role) {
        this.role = role;
    }
}
