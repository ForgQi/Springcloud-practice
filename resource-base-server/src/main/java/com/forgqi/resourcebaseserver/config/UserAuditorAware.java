package com.forgqi.resourcebaseserver.config;

import com.forgqi.resourcebaseserver.common.UserHelper;
import com.forgqi.resourcebaseserver.entity.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.lang.NonNull;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
public class UserAuditorAware implements AuditorAware<String> {
    @Override
    @NonNull
    public Optional<String> getCurrentAuditor() {
        return UserHelper.getUserBySecurityContext().map(User::getUsername);
    }
}
