package com.forgqi.resourcebaseserver.config;

import com.forgqi.resourcebaseserver.common.util.UserHelper;
import com.forgqi.resourcebaseserver.entity.User;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.elasticsearch.repository.config.EnableReactiveElasticsearchRepositories;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.lang.NonNull;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
@EnableElasticsearchRepositories(basePackages = "com.forgqi.resourcebaseserver.repository.elasticsearch")
@EnableReactiveElasticsearchRepositories(basePackages = "com.forgqi.resourcebaseserver.repository.elasticsearch")
@EnableJpaRepositories(value = "com.forgqi.resourcebaseserver.repository.jpa", repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class)
@EnableRedisRepositories(basePackages = "com.forgqi.resourcebaseserver.repository.redis")
public class UserAuditorAware implements AuditorAware<String> {
    @Override
    @NonNull
    public Optional<String> getCurrentAuditor() {
        return UserHelper.getUserBySecurityContext().map(User::getUsername).or(() -> Optional.of("anonymousUser"));
    }

    @Bean
    public HttpTraceRepository getTraceRepository() {
        return new InMemoryHttpTraceRepository();
    }
}
