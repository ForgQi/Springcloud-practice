package com.forgqi.resourcebaseserver.repository;

import com.forgqi.resourcebaseserver.entity.Comment;
import com.forgqi.resourcebaseserver.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllByPostEquals(Post post, Pageable pageable);
}