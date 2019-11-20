package com.forgqi.resourcebaseserver.dto;

import com.forgqi.resourcebaseserver.common.UserHelper;

import java.util.HashMap;
import java.util.Map;

public class LoginDTO {
    private String userName;
    private String password;
    private String nickName;
    private String avatar;
    private String clientId;
    private String clientSecret;

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public Map<String, String> convertToTokenMap(){
        Map<String, String> map = new HashMap<>();
        map.put("username", userName);
        map.put("password", password);
        map.put("grant_type", "password");
        map.put("client_id", clientId);
        map.put("client_secret", clientSecret);
        return map;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }
}

