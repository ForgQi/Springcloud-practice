package com.forgqi.resourcebaseserver.repository;

import com.forgqi.resourcebaseserver.dto.IPostDTO;
import com.forgqi.resourcebaseserver.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;

public interface PostRepository extends RevisionRepository<Post, Long, Integer>, JpaRepository<Post, Long> {
    Page<IPostDTO> findBySubjectEquals(String subject, Pageable pageable);
}
