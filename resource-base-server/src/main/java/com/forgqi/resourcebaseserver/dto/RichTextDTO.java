package com.forgqi.resourcebaseserver.dto;

import com.forgqi.resourcebaseserver.dto.Util.RichTextHelper;
import com.forgqi.resourcebaseserver.entity.Post;
import com.forgqi.resourcebaseserver.entity.User;

import java.util.List;
import java.util.Map;

public class RichTextDTO {
    private String title;
    private String html;

    private List<String> imageUrl;
    private List<String> topicList;
    private Map<String, ?> profile;
    public Post convertToPost(User user, String subject){
        Post post = new Post();
        post.setContent(html);
        post.setSubject(subject);
        post.setImageUrl(imageUrl);
        post.setSummary(new RichTextHelper(html).parseSummary());
        post.setTitle(title);
        post.setUser(user);
        return post;
    }
    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public Map<String, ?> getProfile() {
        return profile;
    }

    public void setProfile(Map<String, ?> profile) {
        this.profile = profile;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getTopicList() {
        return topicList;
    }

    public void setTopicList(List<String> topicList) {
        this.topicList = topicList;
    }
    public List<String> getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(List<String> imageUrl) {
        this.imageUrl = imageUrl;
    }
}
