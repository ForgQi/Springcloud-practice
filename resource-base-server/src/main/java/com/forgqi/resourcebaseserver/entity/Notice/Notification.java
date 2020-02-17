package com.forgqi.resourcebaseserver.entity.Notice;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Data
@Embeddable
public class Notification {

    private String title;

    @Column(length = 511)
    private String content;

    @Embedded
    private ClickAction clickAction;

    @Embeddable
    @Getter
    @Setter
    public static class ClickAction {
        private String url;
        private String intent;
    }
}
