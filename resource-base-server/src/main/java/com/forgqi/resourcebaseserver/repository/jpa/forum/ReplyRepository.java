package com.forgqi.resourcebaseserver.repository.jpa.forum;

import com.forgqi.resourcebaseserver.entity.forum.Comment;
import com.forgqi.resourcebaseserver.entity.forum.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;

public interface ReplyRepository extends RevisionRepository<Reply, Long, Integer>, JpaRepository<Reply, Long> {
    Page<Reply> findAllByCommentEquals(Comment comment, Pageable pageable);
}
