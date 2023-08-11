package com.zerobase.nsbackend.errand.dto;

import com.zerobase.nsbackend.errand.domain.entity.Errand;
import com.zerobase.nsbackend.errand.domain.vo.ErrandStatus;
import com.zerobase.nsbackend.errand.domain.vo.PayDivision;
import com.zerobase.nsbackend.global.dto.AddressDto;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ErrandDto {
  private Long id;
  private String title;
  private String content;
  private List<String> images;
  private PayDivision payDivision;
  private Integer pay;
  private LocalDate deadLine;
  private List<String> hashtags;
  private AddressDto address;
  private boolean isLiked;
  private Integer likedCount;
  private ErrandStatus status;
  private Integer viewCount;
  private LocalDateTime createdAt;

  public static ErrandDto from(Errand errand) {
    return ErrandDto.builder()
        .id(errand.getId())
        .title(errand.getTitle())
        .content(errand.getContent())
        .images(errand.getImagesAsStringList())
        .payDivision(errand.getPayDivision())
        .pay(errand.getPay())
        .deadLine(errand.getDeadLine())
        .hashtags(errand.getHashtagsAsStringList())
        .address(AddressDto.from(errand.getAddress()))
        .likedCount(errand.getLikedCount())
        .status(errand.getStatus())
        .viewCount(errand.getViewCount())
        .createdAt(errand.getCreatedAt())
        .build();
  }

  /**
   * Errand 엔티티와 Like 여부와 함께 ErrandDto를 만듭니다.
   * @param errand
   * @param isLiked
   * @return
   */
  public static ErrandDto from(Errand errand, boolean isLiked) {
    ErrandDto errandDto = ErrandDto.from(errand);
    errandDto.setIsLiked(isLiked);
    return errandDto;
  }

  /**
   * isLiked를 변경합니다.
   * 좋아요 여부를 확인하려면 현재 로그인한 유저를 받아와야 하는데,
   * DTO 내에 AuthManager 의존성을 추가해 주는것이 맞지 않다고 생각했습니다.
   * @param isLiked
   */
  private void setIsLiked(boolean isLiked) {
    this.isLiked = isLiked;
  }
}
