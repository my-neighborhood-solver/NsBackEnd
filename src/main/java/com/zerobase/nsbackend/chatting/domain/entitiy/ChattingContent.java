package com.zerobase.nsbackend.chatting.domain.entitiy;

import com.zerobase.nsbackend.member.domain.Member;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
public class ChattingContent {

  @Id
  @GeneratedValue( strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "chatting_room_id", referencedColumnName = "id")
  private ChattingRoom chattingRoom;

  @ManyToOne
  @JoinColumn(name = "sender_id", referencedColumnName = "id")
  private Member sender;

  @Column
  private String nickName;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String content;

  @Column(nullable = false)
  private boolean isRead;

  @Column(nullable = false)
  private LocalDateTime contentTime;

  @PrePersist
  public void prePersist() {
    contentTime = LocalDateTime.now();
  }

  public void setNickname(){
    nickName = sender.getNickname();
  }
}
