package com.zerobase.nsbackend.errand.dto;

import com.zerobase.nsbackend.errand.domain.entity.Performer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class PerformerDto {
  private Long errandId;
  private Long memberId;
  private String nickname;
  private String profileUrl;
  private String status;
  private String requestedAt;

  public static PerformerDto from(Performer performer) {
    return PerformerDto.builder()
        .errandId(performer.getId())
        .memberId(performer.getMember().getId())
        .nickname(performer.getMember().getNickname())
        .profileUrl(performer.getMember().getProfileImage())
        .status(performer.getStatus().name())
        .requestedAt(performer.getCreatedAt().toString())
        .build();
  }
}
