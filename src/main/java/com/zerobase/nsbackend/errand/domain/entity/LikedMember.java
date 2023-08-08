package com.zerobase.nsbackend.errand.domain.entity;

import com.zerobase.nsbackend.member.domain.Member;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class LikedMember {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne
  private Errand errand;

  @ManyToOne
  private Member member;

  private LikedMember(Errand errand, Member member) {
    this.errand = errand;
    this.member = member;
  }

  public static LikedMember of(Errand errand, Member member){
    return new LikedMember(errand, member);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LikedMember that = (LikedMember) o;
    return errand.equals(that.errand) && member.equals(that.member);
  }

  @Override
  public int hashCode() {
    return Objects.hash(errand, member);
  }
}
