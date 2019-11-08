package com.forgqi.resourcebaseserver.repository;

import com.forgqi.resourcebaseserver.entity.UserFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<UserFile, Long> {
    UserFile deleteByPath(String path);
}
