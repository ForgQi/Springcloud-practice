package com.forgqi.authenticationserver.repository;

import com.forgqi.authenticationserver.entity.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SysRoleRepository extends JpaRepository<SysRole, Long> {
    SysRole findFirstByName(String name);
}
