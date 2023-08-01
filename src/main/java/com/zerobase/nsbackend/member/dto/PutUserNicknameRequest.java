package com.zerobase.nsbackend.member.dto;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PutUserNicknameRequest {
    @NotBlank
    private String nickname;
}
