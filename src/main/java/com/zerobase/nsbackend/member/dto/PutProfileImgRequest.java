package com.zerobase.nsbackend.member.dto;

import com.sun.istack.NotNull;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class PutProfileImgRequest {
    @NotBlank
    private MultipartFile img;
}
