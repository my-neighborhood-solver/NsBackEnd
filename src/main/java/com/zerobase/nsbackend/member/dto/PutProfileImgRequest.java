package com.zerobase.nsbackend.member.dto;

import com.sun.istack.NotNull;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PutProfileImgRequest {
    @NotBlank
    private String img;
}
