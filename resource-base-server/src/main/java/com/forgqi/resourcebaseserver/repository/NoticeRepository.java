package com.forgqi.resourcebaseserver.repository;

import com.forgqi.resourcebaseserver.entity.Notice;
import com.forgqi.resourcebaseserver.service.dto.IPostDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.constraints.Min;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    Page<Notice> findAllByRegistrationTokensIn(List<String> registrationTokens, Pageable pageable);

    Page<Notice> findAllByNotificationChannel(String notificationChannel, Pageable pageable);

    Page<Notice> findDistinctByRegistrationTokensInAndNotificationChannelIn(Collection<String> registrationTokens, Collection<String> notificationChannel, Pageable pageable);

    Page<Notice> findAllByOriginalSourceUserId(long originalSourceUser_id, Pageable pageable);
}
