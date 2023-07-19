package com.zerobase.nsbackend.member.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    EXIST_USER_EMAIL("이미 사용 중인 이메일입니다."),
    NOT_EXIST_USER_EMAIL("존재하지 않는 이메일입니다.");

    private final String description;
}
