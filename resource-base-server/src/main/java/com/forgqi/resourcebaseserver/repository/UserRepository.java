package com.forgqi.resourcebaseserver.repository;

import com.forgqi.resourcebaseserver.entity.GradeOnly;
import com.forgqi.resourcebaseserver.entity.SysRole;
import com.forgqi.resourcebaseserver.entity.User;
import com.forgqi.resourcebaseserver.service.dto.IUserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String userName);

    Optional<IUserDTO> findUserById(long id);

    Stream<GradeOnly> findAllBy();

    Page<User> findDistinctByRolesIn(Collection<SysRole> roles, Pageable pageable);
}
