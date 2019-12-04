package com.forgqi.resourcebaseserver.service;

import com.forgqi.resourcebaseserver.common.ForumType;
import com.forgqi.resourcebaseserver.common.errors.OperationException;
import com.forgqi.resourcebaseserver.common.util.UserHelper;
import com.forgqi.resourcebaseserver.entity.*;
import com.forgqi.resourcebaseserver.repository.*;
import com.forgqi.resourcebaseserver.service.dto.ContentDTO;
import com.forgqi.resourcebaseserver.service.dto.RichTextDTO;
import org.springframework.stereotype.Service;

@Service
public class ForumService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;
    private final UserRepository userRepository;
    private final VoteRepository voteRepository;

    public ForumService(PostRepository postRepository, CommentRepository commentRepository, ReplyRepository replyRepository, UserRepository userRepository, VoteRepository voteRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.replyRepository = replyRepository;
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
    }

    public Post savePost(RichTextDTO richTextDTO, String subject) {
        // UserHelper.reloadUserFromSecurityContext(u -> BeanUtils.copyProperties(byId.get(), u));
        return UserHelper.getUserBySecurityContext()
                .flatMap(user -> userRepository.findById(user.getId()))
                .map(user -> postRepository.save(richTextDTO.convertToPost(user, subject))).get();
    }

    public Comment saveComment(ContentDTO contentDTO, Long id) {
        return UserHelper.getUserBySecurityContext()
                .map(user -> commentRepository.save(contentDTO.convertToComment(user, postRepository.getOne(id)))).get();
    }

    public Reply saveReply(ContentDTO contentDTO, Long id) {
        return UserHelper.getUserBySecurityContext()
                .map(user -> replyRepository.save(contentDTO.convertToReply(user, commentRepository.getOne(id)))).get();
    }

    public void deletePost(Long id) {
        User user = UserHelper.getUserBySecurityContext().orElseThrow();
        postRepository.findById(id).map(Post::getUser)
                .filter(user1 -> user1.getId() == user.getId())
                .ifPresentOrElse(user1 -> postRepository.deleteById(id), () -> user.getRoles().stream()
                        .filter(role -> role.getRole().equals("ROLE_ADMIN"))
                        .findAny()
                        .ifPresentOrElse(role -> postRepository.deleteById(id), () -> {
                            throw new OperationException("无权删除");
                        }));
    }

    public void deleteComment(Long id) {
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

    public void deleteReply(Long id) {
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

    public Post upPost(Long id) {
        long user = UserHelper.getUserBySecurityContext().orElseThrow().getId();
        voteRepository.findByUserIdAndTargetIdAndForumType(user, id, ForumType.POST)
                .ifPresent(vote -> {
                    throw new OperationException("请勿重复点赞或踩");
                });
        voteRepository.save(new Vote(ForumType.POST, id, user));
        return upVote(postRepository.findById(id).orElseThrow());
    }

    public Post downPost(Long id) {
        long user = UserHelper.getUserBySecurityContext().orElseThrow().getId();
        voteRepository.findByUserIdAndTargetIdAndForumType(user, id, ForumType.POST)
                .ifPresent(vote -> {
                    throw new OperationException("请勿重复点赞或踩");
                });
        voteRepository.save(new Vote(ForumType.POST, id, user));
        return downVote(postRepository.findById(id).orElseThrow());
    }

    public Comment upComment(Long id) {
        long user = UserHelper.getUserBySecurityContext().orElseThrow().getId();
        voteRepository.findByUserIdAndTargetIdAndForumType(user, id, ForumType.COMMENT)
                .ifPresent(vote -> {
                    throw new OperationException("请勿重复点赞或踩");
                });
        voteRepository.save(new Vote(ForumType.COMMENT, id, user));
        return upVote(commentRepository.findById(id).orElseThrow());
    }

    public Comment downComment(Long id) {
        long user = UserHelper.getUserBySecurityContext().orElseThrow().getId();
        voteRepository.findByUserIdAndTargetIdAndForumType(user, id, ForumType.COMMENT)
                .ifPresent(vote -> {
                    throw new OperationException("请勿重复点赞或踩");
                });
        voteRepository.save(new Vote(ForumType.COMMENT, id, user));
        return downVote(commentRepository.findById(id).orElseThrow());
    }

    private Post upVote(Post post) {
        post.setUpVote(post.getUpVote() + 1);
        return postRepository.save(post);
    }

    private Post downVote(Post post) {
        post.setDownVote(post.getDownVote() + 1);
        return postRepository.save(post);
    }

    private Comment upVote(Comment comment) {
        comment.setUpVote(comment.getUpVote() + 1);
        return commentRepository.save(comment);
    }

    private Comment downVote(Comment comment) {
        comment.setDownVote(comment.getDownVote() + 1);
        return commentRepository.save(comment);
    }
}
