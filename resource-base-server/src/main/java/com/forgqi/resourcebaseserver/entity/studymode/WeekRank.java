package com.forgqi.resourcebaseserver.entity.studymode;

import com.forgqi.resourcebaseserver.entity.forum.IVoteEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@EqualsAndHashCode(callSuper = false)
@Data
@RedisHash("weekRank")
@AllArgsConstructor
public class WeekRank extends IVoteEntity.VoteEntityAdapter {
    @Id
    private Long id;

    private Integer upVote = 0;

    @TimeToLive
    private Long expiration = 60L;
}
