package com.forgqi.resourcebaseserver.service.dto;

import com.forgqi.resourcebaseserver.entity.User;
import com.forgqi.resourcebaseserver.entity.forum.Comment;
import com.forgqi.resourcebaseserver.entity.forum.Post;
import com.forgqi.resourcebaseserver.entity.forum.Reply;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class ContentDTO {
    private String content;
    private Long toUser;
    private String imageUrl;


    public Reply convertToReply(User user, Comment comment) {
        Reply t = new Reply();
        t.setContent(content);
        t.setUser(user);
        t.setComment(comment);
        return t;
    }

    public Comment convertToComment(User user, Post post) {
        Comment t = new Comment();
        BeanUtils.copyProperties(this, t);
//        t.setContent(content);
        t.setUser(user);
        t.setPost(post);
        return t;
    }
}
