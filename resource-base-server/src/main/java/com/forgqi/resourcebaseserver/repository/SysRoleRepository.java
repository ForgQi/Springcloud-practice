package com.forgqi.resourcebaseserver.repository;


import com.forgqi.resourcebaseserver.entity.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SysRoleRepository extends JpaRepository<SysRole, Long> {
    SysRole findFirstByRole(String role);
}
