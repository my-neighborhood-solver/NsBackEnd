package com.zerobase.nsbackend.errand.dto.search;

import com.querydsl.core.annotations.QueryProjection;
import java.util.List;
import lombok.Getter;

@Getter
public class ErrandSearchResult {
  private Long errandId;
  private String title;
  private String content;
  private String imageUrl;  // 의뢰 사진 url (한건)
  private List<String> hashtags; // 해쉬태그
  private Long likeCount;  // 좋아요 수
  private Integer viewCount;  // 조회수
  private String postingDate; // 게시일자
  private String nickname; // 의뢰자 닉네임
  private String userProfile; // 프로필 사진

  @QueryProjection
  public ErrandSearchResult(Long errandId, String title, String content, Long likeCount,
      Integer viewCount, String postingDate, String nickname, String userProfile) {
    this.errandId = errandId;
    this.title = title;
    this.content = content;
    this.likeCount = likeCount;
    this.viewCount = viewCount;
    this.postingDate = postingDate;
    this.nickname = nickname;
    this.userProfile = userProfile;
  }

  public void changeImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public void changeHashtags(List<String> hashtags) {
    this.hashtags = hashtags;
  }

}
