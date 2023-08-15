package com.zerobase.nsbackend.errand.domain.entity;

import com.zerobase.nsbackend.errand.domain.vo.ReviewDivision;
import com.zerobase.nsbackend.errand.domain.vo.ReviewGrade;
import com.zerobase.nsbackend.global.BaseTimeEntity;
import com.zerobase.nsbackend.global.exceptionHandle.ErrorCode;
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

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Review extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne(fetch = FetchType.LAZY)
  private Errand errand;
  @ManyToOne(fetch = FetchType.LAZY)
  private Member reviewer;
  @ManyToOne(fetch = FetchType.LAZY)
  private Member reviewee;
  @Enumerated(EnumType.STRING)
  private ReviewGrade grade;
  private String comment;
  @Enumerated(EnumType.STRING)
  private ReviewDivision division;

  /**
   * 의뢰자 리뷰를 생성합니다. (수행자가 하는 리뷰)
   * @param errand
   * @return
   */
  public static Review of(Errand errand, Member performer,
      ReviewGrade grade, String comment, ReviewDivision division) {
    //   의뢰자 리뷰의 경우
    if (division == ReviewDivision.ERRANDER_REVIEW) {
      return Review.builder()
          .errand(errand)
          .reviewer(performer)
          .reviewee(errand.getErrander())
          .grade(grade)
          .comment(comment)
          .division(ReviewDivision.ERRANDER_REVIEW)
          .build();
    }
    // 수행자 리뷰의 경우
    if (division == ReviewDivision.PERFORMER_REVIEW) {
      return Review.builder()
          .errand(errand)
          .reviewer(errand.getErrander())
          .reviewee(performer)
          .grade(grade)
          .comment(comment)
          .division(ReviewDivision.PERFORMER_REVIEW)
          .build();
    }
    throw new IllegalArgumentException(ErrorCode.INVALID_INPUT_ERROR.getDescription());
  }
}
