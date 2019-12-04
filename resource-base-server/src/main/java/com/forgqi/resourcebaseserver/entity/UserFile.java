package com.forgqi.resourcebaseserver.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class UserFile {
    @Id // 主键
    @Column(nullable = false)
    private String id;
    private String name;
    @Column(nullable = false)
    private String path;
    private String type;
    private String userId;

    public UserFile() {
    }

    public UserFile(String id, String name, String path, String type, String userId) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.type = type;
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
