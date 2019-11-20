package com.forgqi.resourcebaseserver.repository;

import com.forgqi.resourcebaseserver.entity.UserFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Transient;

public interface FileRepository extends JpaRepository<UserFile, String> {
    @Transactional
    Integer deleteUserFileByPath(String path);
}
