package com.forgqi.resourcebaseserver.common.impl;

import com.forgqi.resourcebaseserver.common.Handler;
import com.forgqi.resourcebaseserver.entity.User;

public class OwnerAuthorize extends Handler {
    @Override
    public boolean handleRequest(User user) {
        return true;
    }
}
