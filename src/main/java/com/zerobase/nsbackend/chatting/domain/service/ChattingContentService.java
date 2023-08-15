package com.zerobase.nsbackend.chatting.domain.service;

import com.zerobase.nsbackend.chatting.domain.entity.ChattingContent;
import com.zerobase.nsbackend.chatting.domain.entity.ChattingRoom;
import com.zerobase.nsbackend.chatting.domain.repository.ChattingContentRepository;
import com.zerobase.nsbackend.chatting.domain.repository.ChattingRoomRepository;
import com.zerobase.nsbackend.chatting.dto.ChatContentRequest;
import com.zerobase.nsbackend.chatting.dto.ChatContentResponse;
import com.zerobase.nsbackend.global.exceptionHandle.ErrorCode;
import com.zerobase.nsbackend.member.domain.Member;
import com.zerobase.nsbackend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChattingContentService {

  private final ChattingContentRepository chattingContentRepository;
  private final ChattingRoomRepository chattingRoomRepository;
  private final MemberRepository memberRepository;

  private final SimpMessagingTemplate template;


  // 채팅 내용 저장
  @Transactional
  public ChatContentResponse saveChattingContent(ChatContentRequest request) {
    ChattingRoom chattingRoom = chattingRoomFindById(request.getChattingRoomId());
    Member sender = memberFindById(request.getSenderId());

    ChattingContent saveContent = chattingContentRepository.save(
        ChattingContent.builder()
            .chattingRoom(chattingRoom)
            .sender(sender)
            .content(request.getContent())
            .isRead(false)
            .build());

    ChatContentResponse response = ChatContentResponse.from(saveContent);

    template
        .convertAndSendToUser(String.valueOf(request.getChattingRoomId()), "/private", response);
    return response;
  }

  public Member memberFindById(Long memberId) {
    return memberRepository.findById(memberId)
        .orElseThrow(() -> new IllegalArgumentException(ErrorCode.MEMBER_NOT_FOUND
            .getDescription()));
  }

  public ChattingRoom chattingRoomFindById(Long roomId) {
    return chattingRoomRepository.findById(roomId).orElseThrow(
        () -> new IllegalArgumentException(ErrorCode.CHATTING_NOT_FOUND.getDescription()));
  }
}
