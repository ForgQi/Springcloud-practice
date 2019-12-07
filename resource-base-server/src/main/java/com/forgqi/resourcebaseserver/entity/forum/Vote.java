package com.forgqi.resourcebaseserver.entity.forum;

import com.forgqi.resourcebaseserver.common.Voted;
import com.forgqi.resourcebaseserver.entity.AbstractAuditingEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Vote extends AbstractAuditingEntity {
    @Id // 主键
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增长策略
    private Long id;

    private Voted.Type votedType;

    private Long targetId;

    private Long userId;

    private Voted.State state;

    public Vote(Voted.Type votedType, Long targetId, Long userId, Voted.State state) {
        this.votedType = votedType;
        this.targetId = targetId;
        this.userId = userId;
        this.state = state;
    }

    public Vote() {
//        通过bean Entity实例化必须要有空构造函数
    }
}
