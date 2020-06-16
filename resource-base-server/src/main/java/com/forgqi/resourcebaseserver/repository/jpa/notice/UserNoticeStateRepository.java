package com.forgqi.resourcebaseserver.repository.jpa.notice;

import com.forgqi.resourcebaseserver.entity.Notice.UserNoticeState;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserNoticeStateRepository extends JpaRepository<UserNoticeState, UserNoticeState.UserNotice> {

}
