package com.forgqi.resourcebaseserver.entity.Notice;

import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.io.Serializable;

@Getter
@Setter
@Entity
public class UserNoticeState {

    @EmbeddedId
    private UserNotice id;

    @Data
    @Embeddable
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserNotice implements Serializable {

        private long userId;

        private long noticeId;
    }
}
