package com.forgqi.resourcebaseserver.repository.forum;

import com.forgqi.resourcebaseserver.entity.forum.Post;
import com.forgqi.resourcebaseserver.service.dto.IPostDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;

public interface PostRepository extends RevisionRepository<Post, Long, Integer>, JpaRepository<Post, Long> {
    Page<IPostDTO> findBySubjectEquals(String subject, Pageable pageable);

    Page<IPostDTO> findAllBy(Pageable pageable);
}
