package com.forgqi.resourcebaseserver.entity;

import javax.persistence.*;

@Entity
public class UserFile {
    @Id // 主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增长策略
    @Column(nullable = false)
    private Long id;
    private String name;
    @Column(nullable = false)
    private String path;
    private String type;

    private String md5;

    public UserFile(String name, String path, String type, String md5) {
        this.name = name;
        this.path = path;
        this.type = type;
        this.md5 = md5;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
}
