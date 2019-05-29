package com.forgqi.authenticationserver.repository;

import com.forgqi.authenticationserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
