package com.zerobase.nsbackend.errand.dto;

import com.zerobase.nsbackend.errand.domain.vo.ReviewDivision;
import com.zerobase.nsbackend.errand.domain.vo.ReviewGrade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ReviewErrandRequest {
  private ReviewDivision division;
  private Long revieweeId;
  private ReviewGrade reviewGrade;
  private String comment;

}
