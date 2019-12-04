package com.forgqi.resourcebaseserver.service.impl;

import com.forgqi.resourcebaseserver.common.Voted;
import com.forgqi.resourcebaseserver.common.errors.OperationException;
import com.forgqi.resourcebaseserver.common.util.UserHelper;
import com.forgqi.resourcebaseserver.entity.User;
import com.forgqi.resourcebaseserver.entity.forum.Comment;
import com.forgqi.resourcebaseserver.entity.forum.Post;
import com.forgqi.resourcebaseserver.repository.forum.PostRepository;
import com.forgqi.resourcebaseserver.service.AbstractVoteService;
import com.forgqi.resourcebaseserver.service.ForumService;
import com.forgqi.resourcebaseserver.service.dto.RichTextDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("postsService")
public class PostServiceImpl extends AbstractVoteService<Post> implements ForumService<Post, RichTextDTO, String> {
    private final PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
        repository = postRepository;
        type = Voted.Type.POST;
    }

    @Override
    public Optional<Post> save(RichTextDTO content, String attach) {
        // UserHelper.reloadUserFromSecurityContext(u -> BeanUtils.copyProperties(byId.get(), u));
        return UserHelper.getUserBySecurityContext()
                .map(user -> postRepository.save(content.convertToPost(user, attach)));
    }

    @Override
    public void delete(Long id) {
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

    @Override
    public Optional<Post> update(Long id, String content) {
        return update(postRepository, id, content);
    }

//    public Optional<Post> upVote(Long id) {
//        return upVote(id, postRepository, Voted.Type.POST);
//    }
//
//    public Optional<Post> downVote(Long id) {
//        return downVote(id, postRepository, Voted.Type.POST);
//    }
}
