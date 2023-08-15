package com.zerobase.nsbackend.errand.domain.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ErrandImage {
  private static final String S3_URL = "https://my-neighbor-solver.s3.ap-northeast-2.amazonaws.com/";
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String imageUrl;

  private ErrandImage(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public static ErrandImage of(String imageUrl) {
    return new ErrandImage(imageUrl);
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public String getFullImageUrl() {
    return S3_URL + imageUrl;
  }
}
