package com.forgqi.resourcebaseserver.service;

import com.forgqi.resourcebaseserver.common.util.UserHelper;
import com.forgqi.resourcebaseserver.entity.User;
import com.forgqi.resourcebaseserver.entity.forum.IForum;
import com.forgqi.resourcebaseserver.security.Authorize;
import com.forgqi.resourcebaseserver.security.ForumPermissionManager;
import com.forgqi.resourcebaseserver.service.dto.ContentDTO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

//@SuppressWarnings("rawtypes")
public interface ForumService<R extends IForum<?>> extends ForumPermissionManager {

    default R save(R instance) {
        return getRepository().save(instance);
    }

    @Authorize
    default void delete(Long id) {
        getRepository().deleteById(id);
    }

//    @SuppressWarnings("unchecked")
    default Optional<R> update(Long id, Map<String, ?> editable) {
        CrudRepository<R, Long> repository = getRepository();
        return UserHelper.getUserBySecurityContext().flatMap(user -> repository.findById(id)
                .map(update -> {
                    if (update.getUser().getId() == user.getId()){
                        update.setContent((String) editable.get("content"));
                        update.setImgUrl(editable.get("imageUrl"));
                        return repository.save(update);
                    }
                    return null;
                })
        );
    }

    CrudRepository<R, Long> getRepository();
}
