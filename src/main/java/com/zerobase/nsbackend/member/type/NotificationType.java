package com.zerobase.nsbackend.member.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationType {
    InterestBoard_DeadLine("관심글 목록에 마감일 1일 전인 관심글이 있습니다.");

    private final String description;

}
