package com.forgqi.resourcebaseserver.repository;

import com.forgqi.resourcebaseserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
