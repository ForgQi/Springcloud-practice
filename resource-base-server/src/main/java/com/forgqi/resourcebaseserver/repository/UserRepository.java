package com.forgqi.resourcebaseserver.repository;

import com.forgqi.resourcebaseserver.entity.User;
import com.forgqi.resourcebaseserver.service.dto.IUserDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String userName);

    Optional<IUserDTO> findUserById(long id);
}
