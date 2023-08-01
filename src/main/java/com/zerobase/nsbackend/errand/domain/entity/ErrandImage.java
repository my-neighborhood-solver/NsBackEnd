package com.zerobase.nsbackend.errand.domain.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ErrandImage {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne
  private Errand errand;
  private String imageUrl;

  @Builder
  public ErrandImage(Errand errand, String imageUrl) {
    changeErrand(errand);
    this.imageUrl = imageUrl;
  }

  /**
   * 연관관계 편의 메서드
   */
  public void changeErrand(Errand errand) {
    if (this.errand != null) {
      this.errand.getImages().remove(this);
    }
    this.errand = errand;
    errand.getImages().add(this);
  }

}
