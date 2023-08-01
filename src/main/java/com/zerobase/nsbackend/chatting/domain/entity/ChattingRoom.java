package com.zerobase.nsbackend.chatting.domain.entity;

import com.zerobase.nsbackend.errand.domain.Errand;
import com.zerobase.nsbackend.member.domain.Member;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ChattingRoom {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "errand_id", referencedColumnName = "id")
  private Errand errand;

  @ManyToOne
  @JoinColumn( name = "member_id" , referencedColumnName = "id")
  private Member sender;

  @OneToMany(mappedBy = "chattingRoom", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ChattingContent> chattingContent = new ArrayList<>();

  @Column(nullable = false)
  private LocalDateTime createAt;

  private LocalDateTime exitTimeErrand;
  private LocalDateTime exitTimeMember;

  @PrePersist
  public void prePersist() {
    createAt = LocalDateTime.now();
  }


}
