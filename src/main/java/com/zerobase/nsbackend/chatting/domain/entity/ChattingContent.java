package com.zerobase.nsbackend.chatting.domain.entity;

import com.zerobase.nsbackend.global.BaseTimeEntity;
import com.zerobase.nsbackend.member.domain.Member;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
public class ChattingContent extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "chatting_room_id")
  private ChattingRoom chattingRoom;

  @ManyToOne
  @JoinColumn(name = "sender_id")
  private Member sender;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String content;

  @Column(nullable = false)
  private boolean isRead;

  public void setRead(boolean isRead) {
    this.isRead = isRead;
  }
}
