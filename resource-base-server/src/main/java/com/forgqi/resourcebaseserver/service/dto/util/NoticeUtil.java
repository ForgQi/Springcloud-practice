package com.forgqi.resourcebaseserver.service.dto.util;

import com.forgqi.resourcebaseserver.common.util.UserHelper;
import com.forgqi.resourcebaseserver.entity.Notice.Notice;
import com.forgqi.resourcebaseserver.entity.Notice.Notification;
import com.forgqi.resourcebaseserver.entity.User;

import java.util.List;

public class NoticeUtil {
    public static Notice buildNotice(String channel, String content, String intent, List<String> target) {
        Notice notice = new Notice();
        notice.setNotificationChannel(channel);
        Notification notification = new Notification();
        notification.setContent(content);
        Notification.ClickAction clickAction = new Notification.ClickAction();
        clickAction.setIntent(String.valueOf(intent));
        notification.setClickAction(clickAction);
        notice.setNotification(notification);
        notice.setOriginalSourceUser(new User(UserHelper.getUserIdBySecurityContext()));
        notice.setRegistrationTokens(target);
        return notice;
    }
}
