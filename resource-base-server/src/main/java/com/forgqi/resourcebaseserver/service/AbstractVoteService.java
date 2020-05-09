package com.forgqi.resourcebaseserver.service;

import com.forgqi.resourcebaseserver.common.Voted;
import com.forgqi.resourcebaseserver.common.errors.OperationException;
import com.forgqi.resourcebaseserver.common.util.UserHelper;
import com.forgqi.resourcebaseserver.entity.forum.IVoteEntity;
import com.forgqi.resourcebaseserver.entity.forum.Vote;
import com.forgqi.resourcebaseserver.repository.VoteRepository;
import org.hibernate.StaleStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.retry.annotation.Retryable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public abstract class AbstractVoteService<R extends IVoteEntity> {
    @Autowired
    private VoteRepository voteRepository;

    protected abstract Voted.Type getType();

    protected abstract CrudRepository<R, Long> getRepository();

    void decide(Long id, Voted.State state) {
        Voted.Type type = getType();
        long user = UserHelper.getUserBySecurityContext().orElseThrow().getId();
        voteRepository.findByUserIdAndTargetIdAndVotedType(user, id, type)
                .ifPresent(vote -> {
                    throw new OperationException("请勿重复点赞或踩");
                });
        voteRepository.save(new Vote(type, id, user, state));
    }

    @Retryable(StaleStateException.class)
    @Transactional
    // 枚举使用==判断相同，其重写的equals也是这么判断的
    public Optional<R> vote(Long id, String voteState) {
        CrudRepository<R, Long> repository = getRepository();
        Voted.State state = Voted.State.valueOf(voteState.toUpperCase());
        decide(id, state);
        return repository.findById(id)
                .map(r -> {
                    if (Voted.State.UP == state) {
                        r.setUpVote(r.getUpVote() + 1);
                    } else {
                        r.setDownVote(r.getDownVote() + 1);
                    }
                    return repository.save(r);
                });
    }
}
