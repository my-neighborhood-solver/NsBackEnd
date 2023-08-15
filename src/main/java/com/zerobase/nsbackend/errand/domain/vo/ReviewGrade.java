package com.zerobase.nsbackend.errand.domain.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReviewGrade {
  GOOD("좋아요"),
  NORMAL("보통이에요"),
  POOR("아쉬워요");

  private final String description;
}
