package com.zerobase.nsbackend.errand.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ErrandSearchCondition {
  private String errandTitle;
  private String hashtag;
}
