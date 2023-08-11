package com.zerobase.nsbackend.errand.dto;

import com.zerobase.nsbackend.member.domain.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErranderDto {
  private Long memberId;
  private String nickname;
  private String email;
  private String profileImage;
  private Integer errandCount;

  public static ErranderDto of(Member member, Integer errandCount) {
    return ErranderDto.builder()
        .memberId(member.getId())
        .nickname(member.getNickname())
        .email(member.getEmail())
        .profileImage(member.getProfileImage())
        .errandCount(errandCount)
        .build();
  }
}
