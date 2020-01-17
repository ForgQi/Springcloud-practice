package com.forgqi.resourcebaseserver.repository;

import com.forgqi.resourcebaseserver.entity.Advise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdviseRepository extends JpaRepository<Advise, Long> {

    Optional<Advise> findFirstByOrderByIdDesc();
}
