package com.zerobase.nsbackend.member.dto;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PutProfileImgRequest {
    @NotNull
    private String img;
}
