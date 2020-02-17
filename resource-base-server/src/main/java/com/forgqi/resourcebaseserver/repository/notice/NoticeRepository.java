package com.forgqi.resourcebaseserver.repository.notice;

import com.forgqi.resourcebaseserver.entity.Notice.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    Page<Notice> findAllByRegistrationTokensIn(List<String> registrationTokens, Pageable pageable);

    Page<Notice> findAllByNotificationChannel(String notificationChannel, Pageable pageable);

    Page<Notice> findDistinctByRegistrationTokensInAndNotificationChannelIn(Collection<String> registrationTokens, Collection<String> notificationChannel, Pageable pageable);

    Page<Notice> findAllByOriginalSourceUserId(long originalSourceUser_id, Pageable pageable);
}
