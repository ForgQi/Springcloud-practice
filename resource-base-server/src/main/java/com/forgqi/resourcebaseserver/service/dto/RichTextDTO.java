package com.forgqi.resourcebaseserver.service.dto;

import com.forgqi.resourcebaseserver.entity.User;
import com.forgqi.resourcebaseserver.entity.forum.Post;
import com.forgqi.resourcebaseserver.service.dto.util.RichTextHelper;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class RichTextDTO {
    private String title;
    private String html;

    private List<String> imageUrl;
    private List<String> topicList;
    private Map<String, ?> profile;

    public Post convertToPost(User user, String subject) {
        Post post = new Post();
        post.setContent(html);
        post.setSubject(subject);
        post.setImageUrl(imageUrl);
        post.setSummary(new RichTextHelper(html).parseSummary());
        post.setTitle(title);
        post.setUser(user);
        return post;
    }
}
