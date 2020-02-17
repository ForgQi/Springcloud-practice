package com.forgqi.resourcebaseserver.service.impl;

import com.forgqi.resourcebaseserver.common.Voted;
import com.forgqi.resourcebaseserver.entity.User;
import com.forgqi.resourcebaseserver.entity.forum.Post;
import com.forgqi.resourcebaseserver.repository.forum.PostRepository;
import com.forgqi.resourcebaseserver.service.AbstractVoteService;
import com.forgqi.resourcebaseserver.service.ForumService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.StaleStateException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

@Slf4j
@Service("postsService")
@RequiredArgsConstructor
public class PostServiceImpl extends AbstractVoteService<Post> implements ForumService<Post> {
    private final PostRepository postRepository;

    public boolean decide(User user, Long resourceId) {
        return postRepository.findById(resourceId).map(post -> post.getUser().getId() == user.getId() ||
                user.getAuthorities().stream().anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()))).get();
    }

    @Override
    protected Voted.Type getType() {
        return Voted.Type.POST;
    }

    @Override
    public CrudRepository<Post, Long> getRepository() {
        return postRepository;
    }

    @Transactional
    @Async
    @Retryable(StaleStateException.class)
    public void changeNumSize(Long id, String field) {
        postRepository.findById(id).ifPresent(post -> {
            if ("CommentSize".equals(field)) {
                post.setCommentSize(post.getCommentSize() + 1);
            } else if ("Pv".equals(field)) {
                post.setPv(post.getPv() + 1);
            } else {
                post.setCommentSize((int) post.getCommentList().parallelStream().flatMap(comment -> Stream.concat(Stream.of(comment), comment.getReplyList().stream())).count());
            }
            postRepository.save(post);
        });
    }
}
