package com.zerobase.nsbackend.chatting.domain.service;

import com.zerobase.nsbackend.chatting.domain.entity.ChattingContent;
import com.zerobase.nsbackend.chatting.domain.entity.ChattingRoom;
import com.zerobase.nsbackend.chatting.domain.repository.ChattingContentRepository;
import com.zerobase.nsbackend.chatting.dto.ChatContentRequest;
import com.zerobase.nsbackend.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChattingContentService {

  private final ChattingContentRepository chattingContentRepository;
  private final ChattingRoomService chattingRoomService;


  // 채팅 내용 저장
  @Transactional
  public ChattingContent saveChattingContent(ChatContentRequest request) {
    ChattingRoom chattingRoom = chattingRoomService
        .chattingRoomFindById(request.getChattingRoomId());
    Member sender = chattingRoomService.memberFindById(request.getSenderId());

    return chattingContentRepository.save(
        ChattingContent.builder()
            .chattingRoom(chattingRoom)
            .sender(sender)
            .nickName(sender.getNickname())
            .content(request.getContent())
            .isRead(false)
            .build()
    );
  }
}
