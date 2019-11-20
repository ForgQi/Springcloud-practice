package com.forgqi.resourcebaseserver.controller;

import com.forgqi.resourcebaseserver.dto.ContentDTO;
import com.forgqi.resourcebaseserver.dto.RichTextDTO;
import com.forgqi.resourcebaseserver.entity.Comment;
import com.forgqi.resourcebaseserver.entity.Post;
import com.forgqi.resourcebaseserver.entity.Reply;
import com.forgqi.resourcebaseserver.service.ForumService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
public class ForumController {
    private final ForumService forumService;

    public ForumController(ForumService forumService) {
        this.forumService = forumService;
    }

    @PostMapping(value = "/posts/subjects/{subject}")
    Post push(@RequestBody RichTextDTO richTextDTO, @PathVariable String subject){

        return forumService.savePost(richTextDTO, subject);
    }
    @PostMapping(value = "/posts/{postId}/comments")
    Comment comment(@RequestBody ContentDTO contentDTO, @PathVariable Long postId){
        return forumService.saveComment(contentDTO, postId);
    }
    @PostMapping(value = "/comments/{commentId}/replies")
    Reply reply(@RequestBody ContentDTO contentDTO, @PathVariable Long commentId){
        return forumService.saveReply(contentDTO, commentId);
    }
    @PutMapping(value = "/posts/{id}/up")
    Post upPost(@PathVariable Long id){
        return forumService.upPost(id);
    }
    @PutMapping(value = "/posts/{id}/down")
    Post downPost(@PathVariable Long id){
        return forumService.downPost(id);
    }

    @PutMapping(value = "/comments/{id}/up")
    Comment upComment(@PathVariable Long id){
        return forumService.upComment(id);
    }
    @PutMapping(value = "/comments/{id}/down")
    Comment downComment(@PathVariable Long id){
        return forumService.downComment(id);
    }

    @DeleteMapping(value = "/posts/{id}")
    void deletePost(@PathVariable Long id){
        forumService.deletePost(id);
    }

    @DeleteMapping(value = "/comments/{id}")
    void deleteComment(@PathVariable Long id){
        forumService.deleteComment(id);
    }
    @DeleteMapping(value = "/replies/{id}")
    void deleteReply(@PathVariable Long id){
        forumService.deleteReply(id);
    }
}
