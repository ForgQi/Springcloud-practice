package com.forgqi.resourcebaseserver.repository;

import com.forgqi.resourcebaseserver.common.ForumType;
import com.forgqi.resourcebaseserver.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findByUserIdAndTargetIdAndForumType(Long userId, Long targetId, ForumType forumType);
}
