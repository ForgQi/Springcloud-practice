package com.forgqi.resourcebaseserver.entity;

import com.forgqi.resourcebaseserver.common.ForumType;

import javax.persistence.*;

@Entity
public class Vote extends AbstractAuditingEntity {
    @Id // 主键
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增长策略
    private Long id;

    private ForumType forumType;

    private Long targetId;

    private Long userId;

    public Vote(ForumType forumType, Long targetId, Long userId) {
        this.forumType = forumType;
        this.targetId = targetId;
        this.userId = userId;
    }

    public Vote() {
//        通过bean Entity实例化必须要有空构造函数
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ForumType getForumType() {
        return forumType;
    }

    public void setForumType(ForumType forumType) {
        this.forumType = forumType;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
