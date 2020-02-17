package com.forgqi.resourcebaseserver.service.dto;

import com.forgqi.resourcebaseserver.common.util.UserHelper;
import com.forgqi.resourcebaseserver.entity.forum.Comment;
import com.forgqi.resourcebaseserver.entity.forum.Post;
import com.forgqi.resourcebaseserver.entity.forum.Reply;
import com.forgqi.resourcebaseserver.service.dto.util.RichTextHelper;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.data.util.CastUtils;

import java.util.List;
import java.util.Map;

@Data
public class ContentDTO {
    private String content;
    private Long toUser;
//    private String imageUrl;


    private String title;
    private String html;

    private Object imageUrl;
    private List<String> topicList;
    private Map<String, ?> profile;

    public Reply convertToReply(Comment comment) {
        return UserHelper.getUserBySecurityContext().map(user -> {
            Reply t = new Reply();
            BeanUtils.copyProperties(this, t);
            t.setImageUrl((String) imageUrl);
            t.setUser(user);
            t.setComment(comment);
            return t;
        }).get();

    }

    public Comment convertToComment(Post post) {
        return UserHelper.getUserBySecurityContext().map(user -> {
            Comment t = new Comment();
            BeanUtils.copyProperties(this, t);
            t.setImageUrl((String) imageUrl);
            t.setUser(user);
            t.setPost(post);
            return t;
        }).get();
    }

    public Post convertToPost(String subject) {
        return UserHelper.getUserBySecurityContext().map(user -> {
            Post post = new Post();
            BeanUtils.copyProperties(this, post);
            post.setImageUrl(CastUtils.cast(imageUrl));
            post.setContent(html);
            post.setSubject(subject);
            post.setSummary(new RichTextHelper(html).parseSummary());
            post.setUser(user);
            return post;
        }).get();
    }
}
