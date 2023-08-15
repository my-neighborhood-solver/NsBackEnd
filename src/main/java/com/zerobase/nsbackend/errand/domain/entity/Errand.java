package com.zerobase.nsbackend.errand.domain.entity;

import com.zerobase.nsbackend.errand.domain.vo.ErrandStatus;
import com.zerobase.nsbackend.errand.domain.vo.PayDivision;
import com.zerobase.nsbackend.global.BaseTimeEntity;
import com.zerobase.nsbackend.global.vo.Address;
import com.zerobase.nsbackend.member.domain.Member;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Errand extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne
  private Member errander;    // 의뢰자
  @Column(length = 500)
  private String title;
  @Column(columnDefinition = "TEXT")
  private String content;
  @Builder.Default
  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "ERRAND_ID")
  private List<ErrandImage> images = new ArrayList<>();
  @Enumerated(EnumType.STRING)
  private PayDivision payDivision;
  private Integer pay;
  private LocalDate deadLine;
  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "streetAddress", column = @Column(name = "street_address")),
      @AttributeOverride(name = "latitude", column = @Column(name = "latitude")),
      @AttributeOverride(name = "longitude", column = @Column(name = "longitude"))
  })
  private Address address;
  @Enumerated(EnumType.STRING)
  private ErrandStatus status;
  @Builder.Default
  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "ERRAND_ID")
  private Set<ErrandHashtag> hashtags = new HashSet<>();

  @Builder.Default
  @OneToMany(mappedBy = "errand", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<LikedMember> likedMembers = new HashSet<>();
  private Integer viewCount;

  public void edit(String title, String content, PayDivision payDivision, Integer pay) {
    this.title = title;
    this.content = content;
    this.payDivision = payDivision;
    this.pay = pay;
  }

  public List<String> getImagesAsStringList() {
    return images.stream().map(ErrandImage::getFullImageUrl).collect(Collectors.toList());
  }

  public void changeStatus(ErrandStatus status) {
    this.status = status;
  }

  public void addHashtag(String tagName) {
    hashtags.add(ErrandHashtag.of(tagName));
  }

  public void removeHashtag(String tagName) {
    hashtags.remove(ErrandHashtag.of(tagName));
  }

  public List<String> getHashtagsAsStringList() {
    return hashtags.stream().map(ErrandHashtag::getName).collect(Collectors.toList());
  }

  public void changeAddress(Address address) {
    this.address = address;
  }

  public void like(Member member) {
    LikedMember likedMember = LikedMember.of(this, member);
    if (likedMembers.contains(likedMember)) {
      likedMembers.remove(likedMember);
      return;
    }
    likedMembers.add(likedMember);
  }

  /**
   * 회원이 해당 의뢰에 대해 좋아요를 눌렀는지 체크합니다.
   * @param member
   * @return
   */
  public boolean checkLiked(Member member) {
    return likedMembers.contains(LikedMember.of(this, member));
  }

  public Integer getLikedCount() {
    return likedMembers.size();
  }

  /**
   * 조회 수 1 증가
   */
  public void increaseViewCount() {
    viewCount += 1;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Errand errand = (Errand) o;
    return id.equals(errand.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}

