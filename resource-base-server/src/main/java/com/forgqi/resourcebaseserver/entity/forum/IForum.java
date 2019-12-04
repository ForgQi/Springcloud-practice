package com.forgqi.resourcebaseserver.entity.forum;

import com.forgqi.resourcebaseserver.entity.User;

public interface IForum {
    User getUser();

    void setUser(User user);

    String getContent();

    void setContent(String content);
}
