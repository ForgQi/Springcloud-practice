package com.forgqi.resourcebaseserver.service.impl;

import com.forgqi.resourcebaseserver.entity.User;
import com.forgqi.resourcebaseserver.entity.forum.Comment;
import com.forgqi.resourcebaseserver.entity.forum.Reply;
import com.forgqi.resourcebaseserver.repository.forum.CommentRepository;
import com.forgqi.resourcebaseserver.repository.forum.ReplyRepository;
import com.forgqi.resourcebaseserver.service.ForumService;
import com.forgqi.resourcebaseserver.service.dto.ContentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service("repliesService")
@RequiredArgsConstructor
public class ReplyServiceImpl implements ForumService<Reply, ContentDTO, Long> {
    private final ReplyRepository replyRepository;
    private final PostServiceImpl postService;
    private final CommentRepository commentRepository;

    @Override
    public Reply packageInstance(User user, ContentDTO content, Long attach) {
        return content.convertToReply(user, new Comment(attach));
    }

    @Override
    public CrudRepository<Reply, Long> getRepository() {
        return replyRepository;
    }

    public boolean decide(User user, Long resourceId) {
        return replyRepository.findById(resourceId).map(reply -> {
            if (reply.getUser().getId() == user.getId()) {
                return true;
            }
            return postService.decide(user, reply.getComment().getPost().getId());
        }).get();
    }
}
