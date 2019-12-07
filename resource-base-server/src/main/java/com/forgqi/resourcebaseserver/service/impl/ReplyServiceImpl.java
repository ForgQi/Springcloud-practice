package com.forgqi.resourcebaseserver.service.impl;

import com.forgqi.resourcebaseserver.common.errors.OperationException;
import com.forgqi.resourcebaseserver.common.util.UserHelper;
import com.forgqi.resourcebaseserver.entity.User;
import com.forgqi.resourcebaseserver.entity.forum.Comment;
import com.forgqi.resourcebaseserver.entity.forum.Reply;
import com.forgqi.resourcebaseserver.repository.forum.ReplyRepository;
import com.forgqi.resourcebaseserver.service.ForumService;
import com.forgqi.resourcebaseserver.service.dto.ContentDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("repliesService")
public class ReplyServiceImpl implements ForumService<Reply, ContentDTO, Long> {
    private final ReplyRepository replyRepository;

    public ReplyServiceImpl(ReplyRepository replyRepository) {
        this.replyRepository = replyRepository;
    }

    @Override
    public Optional<Reply> save(ContentDTO content, Long attach) {
        return UserHelper.getUserBySecurityContext()
                .map(user -> replyRepository.save(content.convertToReply(user, new Comment(attach))));
    }

    @Override
    public void delete(Long id) {
        User user = UserHelper.getUserBySecurityContext().orElseThrow();
        replyRepository.findById(id).map(reply -> {
            if (reply.getUser().getId() == user.getId() || reply.getComment().getPost().getUser().getId() == user.getId()) {
                replyRepository.delete(reply);
                return null;
            }
            return user;
        }).ifPresent(user1 -> user1.getRoles().stream()
                .filter(role -> role.getRole().equals("ROLE_ADMIN"))
                .findAny()
                .ifPresentOrElse(role -> replyRepository.deleteById(id), () -> {
                    throw new OperationException("无权删除");
                }));
    }

    @Override
    public Optional<Reply> update(Long id, String content) {
        return update(replyRepository, id, content);
    }
}
