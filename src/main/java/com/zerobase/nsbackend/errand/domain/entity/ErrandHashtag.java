package com.zerobase.nsbackend.errand.domain.entity;

import java.util.Objects;
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
public class ErrandHashtag {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne
  @JoinColumn(name = "ERRAND_ID")
  private Errand errand;

  private String name;

  private ErrandHashtag(String hashtagName) {
    this.name = hashtagName;
  }

  public static ErrandHashtag of(String hashtagName) {
    return new ErrandHashtag(hashtagName);
  }
  public String getName() {
    return name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ErrandHashtag that = (ErrandHashtag) o;
    return name.equals(that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }
}
