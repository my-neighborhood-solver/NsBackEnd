package com.zerobase.nsbackend.member.dto;

import com.zerobase.nsbackend.member.domain.Notification;
import com.zerobase.nsbackend.member.type.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationResponse {
    private String content;
    private NotificationType notificationType;
    private Boolean isRead;

    public static NotificationResponse of(Notification notification){
        return new NotificationResponse(notification.getContent()
            , notification.getNotificationType(), notification.getIsRead());
    }
}
