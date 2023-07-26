package com.zerobase.nsbackend.errand.dto;

import com.zerobase.nsbackend.errand.domain.Errand;
import com.zerobase.nsbackend.errand.domain.vo.PayDivision;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ErrandDto {
  private Long id;
  private String title;
  private String content;
  private PayDivision payDivision;
  private Integer pay;
  private LocalDateTime createdAt;

  public static ErrandDto from(Errand errand) {
    return ErrandDto.builder()
        .id(errand.getId())
        .title(errand.getTitle())
        .content(errand.getContent())
        .payDivision(errand.getPayDivision())
        .pay(errand.getPay())
        .build();
  }
}
