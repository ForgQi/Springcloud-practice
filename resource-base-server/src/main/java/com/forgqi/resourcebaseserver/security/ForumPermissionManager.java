package com.forgqi.resourcebaseserver.security;

import com.forgqi.resourcebaseserver.entity.User;

@FunctionalInterface
public interface ForumPermissionManager {
    boolean decide(User user, Long resourceId);
}
