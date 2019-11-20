package com.forgqi.resourcebaseserver.dto;

import com.forgqi.resourcebaseserver.entity.Comment;
import com.forgqi.resourcebaseserver.entity.Post;
import com.forgqi.resourcebaseserver.entity.Reply;
import com.forgqi.resourcebaseserver.entity.User;

import java.util.Objects;

public class ContentDTO {
    private String content;
    private Long toUser;
    private String imageUrl;


    public Reply convertToReply(User user, Comment comment){
        Reply t = new Reply();
        t.setContent(content);
        t.setUser(user);
        t.setComment(comment);
        return t;
    }
    public Comment convertToComment(User user, Post post){
        Comment t = new Comment();
        t.setContent(content);
        t.setUser(user);
        t.setPost(post);
        return t;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getToUser() {
        return toUser;
    }

    public void setToUser(Long toUser) {
        this.toUser = toUser;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
