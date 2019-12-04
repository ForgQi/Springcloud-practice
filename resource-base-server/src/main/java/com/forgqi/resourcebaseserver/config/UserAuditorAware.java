package com.forgqi.resourcebaseserver.config;

import com.forgqi.resourcebaseserver.common.util.UserHelper;
import com.forgqi.resourcebaseserver.entity.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.lang.NonNull;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(value = "com.forgqi.resourcebaseserver.repository", repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class)
public class UserAuditorAware implements AuditorAware<String> {
    @Override
    @NonNull
    public Optional<String> getCurrentAuditor() {
        return UserHelper.getUserBySecurityContext().map(User::getUsername);
    }
}
