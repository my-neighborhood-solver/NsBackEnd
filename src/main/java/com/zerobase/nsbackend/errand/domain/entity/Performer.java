package com.zerobase.nsbackend.errand.domain.entity;

import com.zerobase.nsbackend.errand.domain.vo.PerformStatus;
import com.zerobase.nsbackend.global.BaseTimeEntity;
import com.zerobase.nsbackend.member.domain.Member;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity
public class Performer extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne(fetch = FetchType.LAZY)
  private Errand errand;
  @ManyToOne(fetch = FetchType.LAZY)
  private Member member;
  @Enumerated(EnumType.STRING)
  private PerformStatus status;

  public static Performer of(Errand errand, Member member) {
    return Performer.builder()
        .errand(errand)
        .member(member)
        .status(PerformStatus.REQUEST)
        .build();
  }

  public void approve() {
    status = PerformStatus.APPROVE;
  }
}
