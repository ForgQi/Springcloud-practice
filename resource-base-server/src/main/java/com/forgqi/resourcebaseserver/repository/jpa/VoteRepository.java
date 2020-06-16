package com.forgqi.resourcebaseserver.repository.jpa;

import com.forgqi.resourcebaseserver.common.Voted;
import com.forgqi.resourcebaseserver.entity.forum.Vote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findByUserIdAndTargetIdAndVotedType(Long userId, Long targetId, Voted.Type votedType);

    Optional<Page<Vote>> findByUserId(Long userId, Pageable pageable);
}
