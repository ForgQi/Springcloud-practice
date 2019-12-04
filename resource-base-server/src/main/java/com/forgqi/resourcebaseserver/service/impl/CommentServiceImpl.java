package com.forgqi.resourcebaseserver.service.impl;

import com.forgqi.resourcebaseserver.common.Voted;
import com.forgqi.resourcebaseserver.common.errors.OperationException;
import com.forgqi.resourcebaseserver.common.util.UserHelper;
import com.forgqi.resourcebaseserver.entity.User;
import com.forgqi.resourcebaseserver.entity.forum.Comment;
import com.forgqi.resourcebaseserver.entity.forum.Post;
import com.forgqi.resourcebaseserver.repository.forum.CommentRepository;
import com.forgqi.resourcebaseserver.service.AbstractVoteService;
import com.forgqi.resourcebaseserver.service.ForumService;
import com.forgqi.resourcebaseserver.service.dto.ContentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("commentsService")
public class CommentServiceImpl extends AbstractVoteService<Comment> implements ForumService<Comment, ContentDTO, Long> {
    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
        repository = commentRepository;
        type = Voted.Type.COMMENT;
    }

    @Override
    public Optional<Comment> save(ContentDTO content, Long attach) {
        return UserHelper.getUserBySecurityContext()
                .map(user -> commentRepository.save(content.convertToComment(user, new Post(attach))));
    }

    @Override
    public void delete(Long id) {
        User user = UserHelper.getUserBySecurityContext().orElseThrow();
        commentRepository.findById(id).map(comment -> {
            if (comment.getUser().getId() == user.getId() || comment.getPost().getUser().getId() == user.getId()) {
                commentRepository.delete(comment);
                return null;
            }
            return user;
        }).ifPresent(user1 -> user1.getRoles().stream()
                .filter(role -> role.getRole().equals("ROLE_ADMIN"))
                .findAny()
                .ifPresentOrElse(role -> commentRepository.deleteById(id), () -> {
                    throw new OperationException("无权删除");
                }));
    }

    @Override
    public Optional<Comment> update(Long id, String content) {
        return update(commentRepository, id, content);
    }

//    @Override
//    public Optional<Comment> upVote(Long id) {
//        return upVote(id, commentRepository, Voted.Type.COMMENT);
//    }
//
//    @Override
//    public Optional<Comment> downVote(Long id) {
//        return downVote(id, commentRepository, Voted.Type.COMMENT);
//    }
}
