package com.zerobase.nsbackend.errand.domain;

import com.zerobase.nsbackend.errand.domain.vo.ErrandStatus;
import com.zerobase.nsbackend.errand.domain.vo.PayDivision;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Errand {
  @Id @GeneratedValue
  private Long id;
  @Column(length = 500)
  private String title;
  @Column(columnDefinition = "TEXT")
  private String content;
  private PayDivision payDivision;
  private Integer pay;
  private ErrandStatus status;
  private Integer viewCount;
  private LocalDateTime createdAt;
}

