package com.zerobase.nsbackend.chatting.dto;

import com.zerobase.nsbackend.chatting.domain.entity.ChattingContent;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ChatContentResponse {

  Long contentId;
  String nickName;
  String content;
  boolean isRead;
  LocalDateTime time;

  public static ChatContentResponse to(ChattingContent chattingContent){
    return ChatContentResponse.builder()
        .contentId(chattingContent.getId())
        .nickName(chattingContent.getSender().getNickname())
        .content(chattingContent.getContent())
        .isRead(chattingContent.isRead())
        .time(chattingContent.getCreatedAt())
        .build();
  }
}
