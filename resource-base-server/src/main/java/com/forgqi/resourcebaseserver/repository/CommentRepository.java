package com.forgqi.resourcebaseserver.repository;

import com.forgqi.resourcebaseserver.entity.Comment;
import com.forgqi.resourcebaseserver.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;

public interface CommentRepository extends RevisionRepository<Comment, Long, Integer>, JpaRepository<Comment, Long> {
    Page<Comment> findAllByPostEquals(Post post, Pageable pageable);
}