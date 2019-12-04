package com.forgqi.resourcebaseserver.repository;

import com.forgqi.resourcebaseserver.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    Optional<Notice> findFirstByOrderByIdDesc();
}
