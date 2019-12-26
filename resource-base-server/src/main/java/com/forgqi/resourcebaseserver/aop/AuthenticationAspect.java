package com.forgqi.resourcebaseserver.aop;

import com.forgqi.resourcebaseserver.common.errors.OperationException;
import com.forgqi.resourcebaseserver.common.util.UserHelper;
import com.forgqi.resourcebaseserver.security.ForumPermissionManager;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class AuthenticationAspect {

    @Before(value = "@annotation(com.forgqi.resourcebaseserver.security.Authorize) && " +
            "args(resourceId)")
    public void authorize(JoinPoint joinPoint, Long resourceId) {
        if (joinPoint.getTarget() instanceof ForumPermissionManager) {
            UserHelper.getUserBySecurityContext().ifPresent(user -> {
                if (!((ForumPermissionManager) joinPoint.getTarget()).decide(user, resourceId)) {
                    log.warn("无权删除:" + resourceId + " " + joinPoint.getTarget().getClass().getSimpleName());
                    throw new OperationException("无权删除");
                }
            });
        }
    }
}
