package com.zerobase.nsbackend.chatting.domain.service;

import com.zerobase.nsbackend.chatting.domain.entity.ChattingRoom;
import com.zerobase.nsbackend.chatting.domain.repository.ChattingRoomRepository;
import com.zerobase.nsbackend.chatting.dto.ChattingRoomCreateResponse;
import com.zerobase.nsbackend.errand.domain.Errand;
import com.zerobase.nsbackend.errand.domain.ErrandService;
import com.zerobase.nsbackend.global.exceptionHandle.ErrorCode;
import com.zerobase.nsbackend.member.domain.Member;
import com.zerobase.nsbackend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChattingRoomService {

  private final ChattingRoomRepository chattingRoomRepository;
  private final MemberRepository memberRepository;

  private final ErrandService errandService;

  // 채팅방 생성
  @Transactional
  public ChattingRoomCreateResponse createChattingRoom(Long errandId,
      Long senderId) {
    // 유효성 검사 member_Id 가 있는지 errand_Id가 있는지
    Errand errand = errandService.getErrand(errandId);

    // member 있는지 검사
    Member sender = memberFindById(senderId);

    ChattingRoom chattingRoom = chattingRoomRepository.findByErrand_MemberAndSender(errand, sender)
        .orElseGet(() -> chattingRoomRepository.save(ChattingRoom.builder()
            .errand(errand)
            .sender(sender)
            .build()));

    return ChattingRoomCreateResponse.from(chattingRoom);
  }

  // member가 있는지 유효성 검사
  private Member memberFindById(Long memberId) {
    return memberRepository.findById(memberId)
        .orElseThrow(() -> new IllegalArgumentException(ErrorCode.MEMBER_NOT_FOUND
            .getDescription()));
  }

  // 채팅방이 존재하는지 유효성 검사
  private ChattingRoom chattingRoomFindById(Long roomId) {
    return chattingRoomRepository.findById(roomId).orElseThrow(
        () -> new IllegalArgumentException(ErrorCode.CHATTING_NOT_FOUND.getDescription()));
  }
}
