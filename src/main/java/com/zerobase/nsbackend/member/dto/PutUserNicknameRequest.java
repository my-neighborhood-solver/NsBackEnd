package com.zerobase.nsbackend.member.dto;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PutUserNicknameRequest {
    @NotNull
    private String nickname;
}
