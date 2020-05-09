package com.forgqi.resourcebaseserver.entity.Notice;

import com.forgqi.resourcebaseserver.entity.AbstractAuditingEntity;
import com.forgqi.resourcebaseserver.entity.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Entity
public class Notice extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增长策略
    @Column(nullable = false)
    private Long id;

    @ElementCollection
    private List<String> registrationTokens;

    private Notification notification;

    private String notificationChannel;

    @Column(name = "is_read")
    private boolean read = false;

    @ElementCollection
    @Column(name = "options")
    private Map<String, String> option;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private User originalSourceUser;
}
