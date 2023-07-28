package com.zerobase.nsbackend.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GetUserResponse {
    private String email;
    private String nickname;
    private String profileImage;
    private String streetNameAddress;
    private Float latitude;
    private Float longitude;
}
