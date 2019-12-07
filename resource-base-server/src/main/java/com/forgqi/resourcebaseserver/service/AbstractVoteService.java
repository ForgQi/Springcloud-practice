package com.forgqi.resourcebaseserver.service;

import com.forgqi.resourcebaseserver.common.Voted;
import com.forgqi.resourcebaseserver.common.errors.OperationException;
import com.forgqi.resourcebaseserver.common.util.UserHelper;
import com.forgqi.resourcebaseserver.entity.forum.IVoteEntity;
import com.forgqi.resourcebaseserver.entity.forum.Vote;
import com.forgqi.resourcebaseserver.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public abstract class AbstractVoteService<R extends IVoteEntity> {
    @Autowired
    private VoteRepository voteRepository;

    protected CrudRepository<R, Long> repository;

    protected Voted.Type type;

    void decide(Long id, Voted.State state){
        decide(id, type, state);
    }

    void decide(Long id, Voted.Type type, Voted.State state){
        long user = UserHelper.getUserBySecurityContext().orElseThrow().getId();
        voteRepository.findByUserIdAndTargetIdAndVotedType(user, id, type)
                .ifPresent(vote -> {
                    throw new OperationException("请勿重复点赞或踩");
                });
        voteRepository.save(new Vote(type, id, user, state));
    }

    // 枚举使用==判断相同，其重写的equals也是这么判断的
    public Optional<R> vote(Long id, String voteState){
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
