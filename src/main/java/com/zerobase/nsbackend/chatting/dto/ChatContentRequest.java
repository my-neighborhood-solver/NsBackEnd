package com.zerobase.nsbackend.chatting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ChatContentRequest {

  private Long chattingRoomId;
  private Long senderId;
  private String content;
}
