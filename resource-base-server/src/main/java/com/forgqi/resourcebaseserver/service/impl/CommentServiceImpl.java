package com.forgqi.resourcebaseserver.service.impl;

import com.forgqi.resourcebaseserver.common.Voted;
import com.forgqi.resourcebaseserver.entity.User;
import com.forgqi.resourcebaseserver.entity.forum.Comment;
import com.forgqi.resourcebaseserver.entity.forum.Post;
import com.forgqi.resourcebaseserver.repository.forum.CommentRepository;
import com.forgqi.resourcebaseserver.service.AbstractVoteService;
import com.forgqi.resourcebaseserver.service.ForumService;
import com.forgqi.resourcebaseserver.service.dto.ContentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service("commentsService")
@RequiredArgsConstructor
public class CommentServiceImpl extends AbstractVoteService<Comment> implements ForumService<Comment, ContentDTO, Long> {
    private final CommentRepository commentRepository;
    private final PostServiceImpl postService;

    @Override
    public Comment packageInstance(User user, ContentDTO content, Long attach) {
        // version仅用来解决Not-null property references a transient value，不会改变version值
        return content.convertToComment(user, new Post(attach, 0L));
    }

    public boolean decide(User user, Long resourceId) {
        return commentRepository.findById(resourceId).map(comment -> {
            if (comment.getUser().getId() == user.getId()) {
                return true;
            }
            return postService.decide(user, comment.getPost().getId());
        }).get();
    }

    @Override
    protected Voted.Type getType() {
        return Voted.Type.COMMENT;
    }

    @Override
    public CrudRepository<Comment, Long> getRepository() {
        return commentRepository;
    }
}
