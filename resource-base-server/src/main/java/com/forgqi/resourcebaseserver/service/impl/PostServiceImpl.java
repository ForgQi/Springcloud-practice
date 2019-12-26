package com.forgqi.resourcebaseserver.service.impl;

import com.forgqi.resourcebaseserver.common.Voted;
import com.forgqi.resourcebaseserver.entity.User;
import com.forgqi.resourcebaseserver.entity.forum.Post;
import com.forgqi.resourcebaseserver.repository.forum.PostRepository;
import com.forgqi.resourcebaseserver.service.AbstractVoteService;
import com.forgqi.resourcebaseserver.service.ForumService;
import com.forgqi.resourcebaseserver.service.dto.RichTextDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service("postsService")
@RequiredArgsConstructor
public class PostServiceImpl extends AbstractVoteService<Post> implements ForumService<Post, RichTextDTO, String> {
    private final PostRepository postRepository;

    @Override
    public Post packageInstance(User user, RichTextDTO content, String attach) {
        return content.convertToPost(user, attach);
    }

    @Override
    protected Voted.Type getType() {
        return Voted.Type.POST;
    }

    @Override
    public CrudRepository<Post, Long> getRepository() {
        return postRepository;
    }

    public boolean decide(User user, Long resourceId) {
        return postRepository.findById(resourceId).map(post -> post.getUser().getId() == user.getId() ||
                user.getAuthorities().stream().anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()))).get();
    }
}
