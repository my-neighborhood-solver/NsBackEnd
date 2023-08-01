package com.zerobase.nsbackend.member.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PutUserAddressRequest {
    @NotBlank
    private String streetNameAddress;
    @NotNull
    private Float latitude;//위도
    @NotNull
    private Float longitude;//경도
}
