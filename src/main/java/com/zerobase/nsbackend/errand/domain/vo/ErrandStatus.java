package com.zerobase.nsbackend.errand.domain.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrandStatus {
  REQUEST("REQUEST", "의뢰중"),
  PERFORMING("PERFORMING", "수행중"),
  FINISH("FINISH", "완료"),
  CANCEL("CANCEL", "취소");

  private final String code;
  private final String description;
}
