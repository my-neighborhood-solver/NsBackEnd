package com.zerobase.nsbackend.errand.domain.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReviewDivision {
  ERRANDER_REVIEW("의뢰자 평가"),
  PERFORMER_REVIEW("수행자 평가");
  private final String description;
}
