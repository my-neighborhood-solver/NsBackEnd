package com.zerobase.nsbackend.errand.domain;

import com.zerobase.nsbackend.errand.domain.vo.ErrandStatus;
import com.zerobase.nsbackend.errand.domain.vo.PayDivision;
import com.zerobase.nsbackend.global.BaseTimeEntity;
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
public class Errand extends BaseTimeEntity {
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

  public void edit(String title, String content, PayDivision payDivision, Integer pay) {
    this.title = title;
    this.content = content;
    this.payDivision = payDivision;
    this.pay = pay;
  }

  public void changeStatus(ErrandStatus status) {
    this.status = status;
  }
}

