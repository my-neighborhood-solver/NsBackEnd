package com.zerobase.nsbackend.errand.dto;

import com.zerobase.nsbackend.errand.domain.vo.PayDivision;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Setter
@NoArgsConstructor
@Getter
@AllArgsConstructor
@Builder
public class ErrandCreateRequest {
  private String title;
  private String content;
  private PayDivision payDivision;
  private Integer pay;
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date deadLine;
  @Builder.Default
  private List<String> hashtags = new ArrayList<>();
}
