package com.forgqi.resourcebaseserver.entity.forum;

import com.forgqi.resourcebaseserver.entity.User;
import org.springframework.data.util.CastUtils;

public interface IForum<T> {
    User getUser();

    void setUser(User user);

    String getContent();

    void setContent(String content);

    T getImageUrl();

    void setImageUrl(T imageUrl);

    default void setImgUrl(Object imageUrl) {
        setImageUrl(CastUtils.cast(imageUrl));
    }
}
