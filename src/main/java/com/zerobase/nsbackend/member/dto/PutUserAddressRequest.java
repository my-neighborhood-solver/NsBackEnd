package com.zerobase.nsbackend.member.dto;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PutUserAddressRequest {
    @NotNull
    private String streetNameAddress;
    @NotNull
    private Float latitude;//위도
    @NotNull
    private Float longitude;//경도
}
