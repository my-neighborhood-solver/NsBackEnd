package com.zerobase.nsbackend.member.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessCode {
    SUCCESS_LOGIN("로그인에 성공하였습니다.");

    private final String description;
}
