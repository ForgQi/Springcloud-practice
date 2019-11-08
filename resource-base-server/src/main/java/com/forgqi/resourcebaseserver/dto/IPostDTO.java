package com.forgqi.resourcebaseserver.dto;

import java.time.Instant;
import java.util.List;

public interface IPostDTO {
    Long getId();

    String getTitle();

    Long getUserId();

    String getAvatar();

    String getSubject();

    String getSummary();

    boolean isAnonymous();

    boolean isSticky();

    boolean isHighlight();

    List<String> getImageUrl();

    Integer getUpVote();

    Integer getDownVote();

    Integer getPv();

    Instant getCreatedDate();

    Instant getLastModifiedDate();
}
