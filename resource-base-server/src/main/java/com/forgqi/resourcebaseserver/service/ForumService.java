package com.forgqi.resourcebaseserver.service;

import com.forgqi.resourcebaseserver.common.UserHelper;
import com.forgqi.resourcebaseserver.dto.ContentDTO;
import com.forgqi.resourcebaseserver.dto.RichTextDTO;
import com.forgqi.resourcebaseserver.entity.Comment;
import com.forgqi.resourcebaseserver.entity.Post;
import com.forgqi.resourcebaseserver.entity.Reply;
import com.forgqi.resourcebaseserver.repository.CommentRepository;
import com.forgqi.resourcebaseserver.repository.PostRepository;
import com.forgqi.resourcebaseserver.repository.ReplyRepository;
import org.springframework.stereotype.Service;

@Service
public class ForumService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;

    public ForumService(PostRepository postRepository, CommentRepository commentRepository, ReplyRepository replyRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.replyRepository = replyRepository;
    }

    public Post savePost(RichTextDTO richTextDTO, String subject){
        return UserHelper.getUserBySecurityContext()
                .map(user -> postRepository.save(richTextDTO.convertToPost(user, subject))).get();
    }
    public Comment saveComment(ContentDTO contentDTO, Long id){
        return UserHelper.getUserBySecurityContext()
                .map(user -> commentRepository.save(contentDTO.convertToComment(user, postRepository.getOne(id)))).get();
    }
    public Reply saveReply(ContentDTO contentDTO, Long id){
        return UserHelper.getUserBySecurityContext()
                .map(user -> replyRepository.save(contentDTO.convertToReply(user, commentRepository.getOne(id)))).get();
    }
}
