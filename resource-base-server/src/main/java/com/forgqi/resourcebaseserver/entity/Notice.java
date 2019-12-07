package com.forgqi.resourcebaseserver.entity;

import lombok.Data;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增长策略
    @Column(nullable = false)
    private Long id;

    @CreatedDate
    @Column(name = "created_date", updatable = false, nullable = false)
    private Instant createdDate;

    private String msg;

    private String url;
}
