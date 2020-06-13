package com.forgqi.resourcebaseserver.service.dto.util;

import com.forgqi.resourcebaseserver.common.util.UserHelper;
import com.forgqi.resourcebaseserver.entity.Notice.Notice;
import com.forgqi.resourcebaseserver.entity.Notice.Notification;
import com.forgqi.resourcebaseserver.entity.User;
import com.forgqi.resourcebaseserver.entity.search.Searchable;
import org.springframework.beans.BeanUtils;
import org.springframework.data.elasticsearch.core.completion.Completion;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

public class ConvertUtil {
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

    public static Searchable convertToSearchable(Object o) {
        Searchable searchable = new Searchable();
        BeanUtils.copyProperties(o, searchable);
        Field field = ReflectionUtils.findField(o.getClass(), "id");
        Field searchableId = ReflectionUtils.findField(Searchable.class, "searchableId");
        assert field != null;
        assert searchableId != null;
        searchableId.setAccessible(true);
        field.setAccessible(true);
        try {
            searchableId.set(searchable, field.get(o));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        searchable.setType(o.getClass().getSimpleName());
        searchable.setTitle(new Completion(Collections.singletonList(new RichTextHelper(searchable.getContent()).parseSummary(50))));
        searchable.setId(null);
        return searchable;
    }
}
