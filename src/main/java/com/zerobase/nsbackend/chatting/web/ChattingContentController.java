package com.zerobase.nsbackend.chatting.web;

import com.zerobase.nsbackend.chatting.domain.entity.ChattingContent;
import com.zerobase.nsbackend.chatting.domain.service.ChattingContentService;
import com.zerobase.nsbackend.chatting.dto.ChatContentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChattingContentController {

  private final ChattingContentService chattingContentService;


  @MessageMapping("/sendMessage")
  @SendTo("/topic/chat")
  public ChattingContent sendMessage(ChatContentRequest request) {
    return chattingContentService.saveChattingContent(request);

  }
}
