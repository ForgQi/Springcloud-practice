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
public class PostController {
    private final ForumService forumService;

    public PostController(ForumService forumService) {
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

}
