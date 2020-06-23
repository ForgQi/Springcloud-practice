package com.forgqi.resourcebaseserver.service.dto;

import com.forgqi.resourcebaseserver.entity.User;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public interface IPostDTO {
    Long getId();

    String getTitle();

    User getUser();

    String getSubject();

    String getSummary();

    boolean isAnonymous();

    boolean isSticky();

    boolean isHighlight();

    List<String> getImageUrl();

    List<String> getTopic();

    Map<String, String> getProfile();

    Integer getUpVote();

    Integer getDownVote();

    Integer getPv();

    Instant getCreatedDate();

    Instant getLastModifiedDate();

    Integer getCommentSize();
}
