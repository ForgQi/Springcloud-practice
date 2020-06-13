package com.forgqi.resourcebaseserver.repository.jpa.notice;

import com.forgqi.resourcebaseserver.entity.Notice.Advice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdviseRepository extends JpaRepository<Advice, Long> {

    Optional<Advice> findFirstByOrderByIdDesc();
}
