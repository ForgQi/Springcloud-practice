package com.forgqi.resourcebaseserver.common.impl;

import com.forgqi.resourcebaseserver.common.Handler;
import com.forgqi.resourcebaseserver.entity.User;

public class AdminAuthorize extends Handler {
    @Override
    public boolean handleRequest(User user) {
        if (user.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()))) {
            return true;
        }
        return successor.handleRequest(user);
    }
}
