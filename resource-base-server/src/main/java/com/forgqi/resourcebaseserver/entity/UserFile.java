package com.forgqi.resourcebaseserver.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFile {
    @Id // 主键
    @Column(nullable = false)
    private String id;
    private String name;
    @Column(nullable = false)
    private String path;
    private String type;
    private String userId;
}
