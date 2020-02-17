package com.forgqi.resourcebaseserver.entity.Notice;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class Advise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增长策略
    @Column(nullable = false)
    private Long id;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private Instant createdDate;

    private String msg;

    private String url;
}
