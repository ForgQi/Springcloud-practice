package com.forgqi.resourcebaseserver.controller;

import com.forgqi.resourcebaseserver.common.errors.NonexistenceException;
import com.forgqi.resourcebaseserver.entity.Comment;
import com.forgqi.resourcebaseserver.entity.Post;
import com.forgqi.resourcebaseserver.entity.Reply;
import com.forgqi.resourcebaseserver.repository.CommentRepository;
import com.forgqi.resourcebaseserver.repository.PostRepository;
import com.forgqi.resourcebaseserver.repository.ReplyRepository;
import com.forgqi.resourcebaseserver.service.dto.IPostDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class BrowseController {
    private final
    PostRepository postRepository;
    private final
    CommentRepository commentRepository;
    private final
    ReplyRepository replyRepository;

    public BrowseController(PostRepository postRepository, CommentRepository commentRepository, ReplyRepository replyRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.replyRepository = replyRepository;
    }

    @GetMapping(value = "/posts/subjects/{subject}")
    Page<IPostDTO> getPage(@PathVariable String subject, @PageableDefault(sort = {"createdDate"}) Pageable pageable) {
        return postRepository.findBySubjectEquals(subject, pageable);
    }

    @GetMapping(value = "/posts/{id}")
    Post getPost(@PathVariable Long id) {
        return postRepository.findById(id).map(post -> {
            post.setPv(post.getPv() + 1);
            return postRepository.save(post);
        }).orElseThrow(() -> new NonexistenceException("帖子不存在"));
    }

    @GetMapping(value = "/posts/{postId}/comments")
    Page<Comment> getComments(@PathVariable Long postId, @PageableDefault(sort = {"createdDate"}) Pageable pageable) {
        return commentRepository.findAllByPostEquals(postRepository.getOne(postId), pageable);
    }

    @GetMapping(value = "/comments/{commentId}/replies")
    Page<Reply> getReplies(@PathVariable Long commentId, @PageableDefault(sort = {"createdDate"}) Pageable pageable) {
        return replyRepository.findAllByCommentEquals(commentRepository.getOne(commentId), pageable);
    }
}
