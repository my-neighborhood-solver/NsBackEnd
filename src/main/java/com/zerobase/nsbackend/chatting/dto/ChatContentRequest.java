package com.zerobase.nsbackend.chatting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ChatContentRequest {

  private Long ChattingRoomId;
  private Long SenderId;
  private String Content;

}
