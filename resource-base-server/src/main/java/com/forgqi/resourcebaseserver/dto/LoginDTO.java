package com.forgqi.resourcebaseserver.dto;

import java.util.HashMap;
import java.util.Map;

public class LoginDTO {
    private String userName;
    private String passWord;
    private String nickname;
    private String avatar;

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }
    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }


    public Map<String, String> convertToMap(){
        Map<String, String> map = new HashMap<>();
        map.put("Login.Token1",this.userName);
        map.put("Login.Token2",this.passWord);
        map.put("goto", "http://my.lzu.edu.cn/loginSuccess.portal");
        map.put("gotoOnFail", "http://my.lzu.edu.cn/loginFailure.portal");
        return map;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

}

