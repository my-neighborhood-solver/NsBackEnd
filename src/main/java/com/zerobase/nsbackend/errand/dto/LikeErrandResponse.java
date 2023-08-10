package com.zerobase.nsbackend.errand.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class LikeErrandResponse {
  private boolean isLiked;

  private LikeErrandResponse(boolean isLiked) {
    this.isLiked = isLiked;
  }

  public static LikeErrandResponse of(boolean isLiked) {
    return new LikeErrandResponse(isLiked);
  }
}
