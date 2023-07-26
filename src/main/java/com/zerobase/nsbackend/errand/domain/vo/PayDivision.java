package com.zerobase.nsbackend.errand.domain.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PayDivision {

  HOURLY("HOURLY", "시급"),
  UNIT("UNIT", "단가");

  private final String code;
  private final String description;
}
