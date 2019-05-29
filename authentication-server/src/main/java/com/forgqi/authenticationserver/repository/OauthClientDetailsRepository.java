package com.forgqi.authenticationserver.repository;

import com.forgqi.authenticationserver.entity.OauthClientDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OauthClientDetailsRepository extends JpaRepository<OauthClientDetails, String> {
}
