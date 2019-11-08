package com.forgqi.resourcebaseserver.dto;

import com.forgqi.resourcebaseserver.entity.Comment;
import com.forgqi.resourcebaseserver.entity.Post;
import com.forgqi.resourcebaseserver.entity.Reply;
import com.forgqi.resourcebaseserver.entity.User;

import java.util.Objects;

public class ContentDTO {
    private String content;

    public Reply convertToReply(User user, Comment comment){
        Reply t = new Reply();
        t.setContent(content);
        t.setUserId(user.getId());
        t.setAvatar(user.getAvatar());
        t.setComment(comment);
        return t;
    }
    public Comment convertToComment(User user, Post post){
        Comment t = new Comment();
        t.setContent(content);
        t.setUserId(user.getId());
        t.setAvatar(user.getAvatar());
        t.setPost(post);
        return t;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
