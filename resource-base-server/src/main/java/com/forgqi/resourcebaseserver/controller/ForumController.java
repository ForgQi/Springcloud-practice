package com.forgqi.resourcebaseserver.controller;

import com.forgqi.resourcebaseserver.entity.forum.Comment;
import com.forgqi.resourcebaseserver.entity.forum.Post;
import com.forgqi.resourcebaseserver.entity.forum.Reply;
import com.forgqi.resourcebaseserver.repository.jpa.forum.CommentRepository;
import com.forgqi.resourcebaseserver.repository.jpa.forum.PostRepository;
import com.forgqi.resourcebaseserver.repository.jpa.notice.NoticeRepository;
import com.forgqi.resourcebaseserver.service.AbstractVoteService;
import com.forgqi.resourcebaseserver.service.ForumService;
import com.forgqi.resourcebaseserver.service.SearchService;
import com.forgqi.resourcebaseserver.service.dto.ContentDTO;
import com.forgqi.resourcebaseserver.service.dto.util.ConvertUtil;
import com.forgqi.resourcebaseserver.service.impl.CommentServiceImpl;
import com.forgqi.resourcebaseserver.service.impl.PostServiceImpl;
import com.forgqi.resourcebaseserver.service.impl.ReplyServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/v1")
//@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class ForumController {

    private final Map<String, ForumService<?>> forumServiceMap;
    private final Map<String, AbstractVoteService<?>> voteServiceMap;
    private final PostServiceImpl postService;
    private final CommentServiceImpl commentService;
    private final ReplyServiceImpl replyService;
    private final CommentRepository commentRepository;
    private final SearchService searchService;
    private final NoticeRepository noticeRepository;
    private final PostRepository postRepository;

    @PostMapping(value = "/posts/subjects/{subject}")
    public Post push(@RequestBody ContentDTO richTextDTO, @PathVariable String subject) {
        Post post = postService.save(richTextDTO.convertToPost(subject));
        searchService.save(ConvertUtil.convertToSearchable(post));
        return post;
    }

    @PostMapping(value = "/posts/{postId}/comments")
    public Comment comment(@RequestBody ContentDTO contentDTO, @PathVariable Long postId) {
        Comment save = commentService.save(contentDTO.convertToComment(new Post(postId, 0L)));
        postService.changeNumSize(postId, "CommentSize");
//        noticeRepository.save(NoticeUtil.buildNotice("social",
//                contentDTO.getContent(),
//                String.valueOf(postId),
//                List.of(String.valueOf(postRepository.findById(postId).orElseThrow().getUser().getId()))
//        ));
        return save;
    }

    @PostMapping(value = "/comments/{commentId}/replies")
    public Reply reply(@RequestBody ContentDTO contentDTO, @PathVariable Long commentId) {
        // version仅用来解决Not-null property references a transient value，不会改变version值
        Reply save = replyService.save(contentDTO.convertToReply(new Comment(commentId, 0L)));
        commentRepository.findById(commentId).ifPresent(comment -> postService.changeNumSize(comment.getPost().getId(), "CommentSize"));
//        Comment comment = commentRepository.findById(commentId).orElseThrow();
//        noticeRepository.save(NoticeUtil.buildNotice("social",
//                contentDTO.getContent(),
//                String.valueOf(comment.getPost().getId()),
//                List.of(String.valueOf(comment.getPost().getUser().getId()),
//                        String.valueOf(comment.getUser().getId()))
//        ));
        return save;
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

    @PutMapping(value = "/posts/{id}/profile")
    public Optional<Post> change(@PathVariable Long id, boolean sticky, @RequestParam(required = false, defaultValue = "false") boolean highlight) {
        return postService.getRepository().findById(id).map(post -> {
            post.setSticky(sticky);
            post.setHighlight(highlight);
            return postService.getRepository().save(post);
        });
    }

    @DeleteMapping(value = "/{service}/{id}")
    public void delete(@PathVariable Long id, @PathVariable String service) {
        if ("comments".equals(service)) {
            commentService.getRepository().findById(id).ifPresent(comment -> {
                forumServiceMap.get(service + "Service").delete(id);
                postService.changeNumSize(comment.getPost().getId(), null);
            });
        } else if ("replies".equals(service)) {
            replyService.getRepository().findById(id).ifPresent(reply -> {
                forumServiceMap.get(service + "Service").delete(id);
                postService.changeNumSize(reply.getComment().getPost().getId(), null);
            });
        } else {
            forumServiceMap.get(service + "Service").delete(id);
        }
    }
}
