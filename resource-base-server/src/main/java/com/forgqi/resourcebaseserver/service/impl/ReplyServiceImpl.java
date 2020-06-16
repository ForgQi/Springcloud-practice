package com.forgqi.resourcebaseserver.service.impl;

import com.forgqi.resourcebaseserver.entity.User;
import com.forgqi.resourcebaseserver.entity.forum.Reply;
import com.forgqi.resourcebaseserver.repository.jpa.forum.CommentRepository;
import com.forgqi.resourcebaseserver.repository.jpa.forum.ReplyRepository;
import com.forgqi.resourcebaseserver.service.ForumService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service("repliesService")
@RequiredArgsConstructor
public class ReplyServiceImpl implements ForumService<Reply> {
    private final ReplyRepository replyRepository;
    private final PostServiceImpl postService;
    private final CommentRepository commentRepository;

    public boolean decide(User user, Long resourceId) {
        return replyRepository.findById(resourceId).map(reply -> {
            if (reply.getUser().getId() == user.getId()) {
                return true;
            }
            return postService.decide(user, reply.getComment().getPost().getId());
        }).get();
    }

    @Override
    public CrudRepository<Reply, Long> getRepository() {
        return replyRepository;
    }
}
