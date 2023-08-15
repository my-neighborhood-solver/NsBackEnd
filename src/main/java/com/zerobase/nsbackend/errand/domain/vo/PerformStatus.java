package com.zerobase.nsbackend.errand.domain.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PerformStatus {
  REQUEST("의뢰 수행 요청"),
  APPROVE("승인");

  private final String description;
}
