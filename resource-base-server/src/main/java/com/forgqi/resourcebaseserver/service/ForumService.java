package com.forgqi.resourcebaseserver.service;

import com.forgqi.resourcebaseserver.common.util.UserHelper;
import com.forgqi.resourcebaseserver.entity.User;
import com.forgqi.resourcebaseserver.entity.forum.IForum;
import com.forgqi.resourcebaseserver.security.Authorize;
import com.forgqi.resourcebaseserver.security.ForumPermissionManager;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface ForumService<R extends IForum, S, T> extends ForumPermissionManager {

    default Optional<R> save(S content, T attach) {
        return UserHelper.getUserBySecurityContext()
                .map(user -> getRepository().save(packageInstance(user, content, attach)));
    }

    @Authorize
    default void delete(Long id) {
        getRepository().deleteById(id);
    }

    default Optional<R> update(Long id, String content) {
        CrudRepository<R, Long> repository = getRepository();
        return UserHelper.getUserBySecurityContext().flatMap(user -> repository.findById(id)
                .map(update -> {
                    if (update.getUser().getId() == user.getId()){
                        update.setContent(content);
                        return repository.save(update);
                    }
                    return null;
                })
        );
    }

    R packageInstance(User user, S content, T attach);

    CrudRepository<R, Long> getRepository();
}
