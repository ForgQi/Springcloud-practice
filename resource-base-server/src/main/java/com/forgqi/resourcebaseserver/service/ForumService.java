package com.forgqi.resourcebaseserver.service;

import com.forgqi.resourcebaseserver.entity.forum.IForum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ForumService<R extends IForum, S, T> {
    Optional<R> save(S content, T attach);

    void delete(Long id);

    default Optional<R> update(JpaRepository<R, Long> repository, Long id, String content) {
        return repository.findById(id)
                .map(update -> {
                    update.setContent(content);
                    return repository.save(update);
                });
    }

    Optional<R> update(Long id, String content);
}
