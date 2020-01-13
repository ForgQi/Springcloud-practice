package com.forgqi.resourcebaseserver.controller;

import com.forgqi.resourcebaseserver.entity.forum.Comment;
import com.forgqi.resourcebaseserver.entity.forum.Post;
import com.forgqi.resourcebaseserver.entity.forum.Reply;
import com.forgqi.resourcebaseserver.service.AbstractVoteService;
import com.forgqi.resourcebaseserver.service.ForumService;
import com.forgqi.resourcebaseserver.service.dto.ContentDTO;
import com.forgqi.resourcebaseserver.service.dto.RichTextDTO;
import com.forgqi.resourcebaseserver.service.impl.CommentServiceImpl;
import com.forgqi.resourcebaseserver.service.impl.PostServiceImpl;
import com.forgqi.resourcebaseserver.service.impl.ReplyServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/v1")
//@RequestMapping
@RequiredArgsConstructor
public class ForumController {

    private final Map<String, ForumService<?, ?, ?>> forumServiceMap;
    private final Map<String, AbstractVoteService<?>> voteServiceMap;
    private final PostServiceImpl postService;
    private final CommentServiceImpl commentService;
    private final ReplyServiceImpl replyService;

    @PostMapping(value = "/posts/subjects/{subject}")
    public Optional<Post> push(@RequestBody RichTextDTO richTextDTO, @PathVariable String subject) {
        return postService.save(richTextDTO, subject);
    }

    @PostMapping(value = "/posts/{postId}/comments")
    public Optional<Comment> comment(@RequestBody ContentDTO contentDTO, @PathVariable Long postId) {
        return commentService.save(contentDTO, postId);
    }

    @PostMapping(value = "/comments/{commentId}/replies")
    public Optional<Reply> reply(@RequestBody ContentDTO contentDTO, @PathVariable Long commentId) {
        return replyService.save(contentDTO, commentId);
    }

    @PutMapping(value = "/{service}/{id}/{state:up|down}")
    public Optional<?> vote(@PathVariable Long id, @PathVariable String service, @PathVariable String state) {
//        AbstractVoteService<?> forumService = voteServiceMap.get(service + "Service");
//        Method voteMethod = forumService.getClass().getMethod(state + "Vote", Long.class);
//        return (Optional<?>) voteMethod.invoke(forumService, id);
        return voteServiceMap.get(service + "Service").vote(id, state);
    }

    @PutMapping(value = "/{service}/{id}")
    public Optional<?> update(@PathVariable Long id, @PathVariable String service, @RequestBody Map<String, ?> editable) {
        return forumServiceMap.get(service + "Service").update(id, editable);
    }

    @DeleteMapping(value = "/{service}/{id}")
    public void delete(@PathVariable Long id, @PathVariable String service) {
        forumServiceMap.get(service + "Service").delete(id);
    }
}
