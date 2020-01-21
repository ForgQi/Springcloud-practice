package com.forgqi.resourcebaseserver.repository.forum;

import com.forgqi.resourcebaseserver.entity.forum.Post;
import com.forgqi.resourcebaseserver.service.dto.IPostDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;

import javax.validation.constraints.Min;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends RevisionRepository<Post, Long, Integer>, JpaRepository<Post, Long> {
    Page<IPostDTO> findBySubjectEqualsAndSticky(String subject, boolean sticky, Pageable pageable);

    Page<IPostDTO> findAllBySticky(boolean sticky, Pageable pageable);

    Page<IPostDTO> findByImageUrlIn(List<String> imageUrl, Pageable pageable);

    Optional<Page<IPostDTO>> getAllByUserId(long user_id, Pageable pageable);
}
