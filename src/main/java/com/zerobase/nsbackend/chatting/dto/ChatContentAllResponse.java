package com.zerobase.nsbackend.chatting.dto;

import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ChatContentAllResponse {
  private Long roomId;
  private String title;
  List<ChatContentResponse> chatContent;

  public static ChatContentAllResponse from(Long roomId, String title, List<ChatContentResponse> response){
    return ChatContentAllResponse.builder()
        .roomId(roomId)
        .title(title)
        .chatContent(response)
        .build();
  }
}
