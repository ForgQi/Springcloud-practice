package com.forgqi.resourcebaseserver.repository;


import com.forgqi.resourcebaseserver.entity.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SysRoleRepository extends JpaRepository<SysRole, Long> {
    SysRole findFirstByRole(String role);
}
