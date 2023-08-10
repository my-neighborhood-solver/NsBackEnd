package com.zerobase.nsbackend.chatting.domain.service;

import static com.zerobase.nsbackend.chatting.type.ChattingRoomCreateStatus.CHATTING_ROOM_CREATE_EXIST;
import static com.zerobase.nsbackend.chatting.type.ChattingRoomCreateStatus.CHATTING_ROOM_CREATE_SUCCESS;

import com.zerobase.nsbackend.chatting.domain.entity.ChattingContent;
import com.zerobase.nsbackend.chatting.domain.entity.ChattingRoom;
import com.zerobase.nsbackend.chatting.domain.repository.ChattingContentRepository;
import com.zerobase.nsbackend.chatting.domain.repository.ChattingRoomRepository;
import com.zerobase.nsbackend.chatting.dto.ChatContentAllResponse;
import com.zerobase.nsbackend.chatting.dto.ChatContentResponse;
import com.zerobase.nsbackend.chatting.dto.ChattingRoomAllResponse;
import com.zerobase.nsbackend.chatting.dto.ChattingRoomCreateResponse;
import com.zerobase.nsbackend.errand.domain.entity.Errand;
import com.zerobase.nsbackend.errand.domain.repository.ErrandRepository;
import com.zerobase.nsbackend.global.exceptionHandle.ErrorCode;
import com.zerobase.nsbackend.member.domain.Member;
import com.zerobase.nsbackend.member.repository.MemberRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChattingRoomService {

  private final ChattingRoomRepository chattingRoomRepository;

  private final ChattingContentRepository chattingContentRepository;

  private final MemberRepository memberRepository;

  private final ErrandRepository errandRepository;

  // 채팅방 생성
  @Transactional
  public ChattingRoomCreateResponse createChattingRoom(Long errandId,
      Long senderId) {
    // 유효성 검사 member_Id 가 있는지 errand_Id가 있는지
    Errand errand = errandRepository.findById(errandId)
        .orElseThrow(
            () -> new IllegalArgumentException(ErrorCode.ERRAND_NOT_FOUND.getDescription()));

    // member 있는지 검사
    Member sender = memberFindById(senderId);

    // errand.memberId != senderId
    if (errand.getErrander().getId() == senderId) {
      throw new IllegalArgumentException(ErrorCode.ERRAND_ID_MEMBER_ID_CONFLICT.getDescription());
    }
    // 이미 채팅방이 존재하는지 검사
    Optional<ChattingRoom> existingChattingRoom = chattingRoomRepository
        .findByErrandAndSender(errand, sender);

    if (existingChattingRoom.isPresent()) {
      // 채팅방이 존재하면 채팅방 번호 리턴
      ChattingRoom chattingRoom = existingChattingRoom.get();

      return ChattingRoomCreateResponse.from(chattingRoom, CHATTING_ROOM_CREATE_EXIST);
    }

    // 채팅방이 존재하지 않으면 새로 생성
    ChattingRoom chattingRoom = chattingRoomRepository.save(ChattingRoom.builder()
        .errand(errand)
        .sender(sender)
        .build());

    return ChattingRoomCreateResponse.from(chattingRoom, CHATTING_ROOM_CREATE_SUCCESS);
  }

  // 채팅방 전체 조회
  @Transactional
  public List<ChattingRoomAllResponse> getChattingRoomsByMemberId(Long memberId) {

    // 유효성 검사 memberId가 있는지
    Member sender = memberFindById(memberId);

    List<ChattingRoom> chattingRooms = chattingRoomRepository
        .findByErrand_Errander_IdOrSenderId(memberId, memberId);
    if (chattingRooms.isEmpty()) {
      throw new IllegalArgumentException(ErrorCode.CHATTING_ROOM_NOT_FOUND.getDescription());
    }

    List<ChattingRoomAllResponse> chattingRoomResponses = new ArrayList<>();

    for (ChattingRoom chattingRoom : chattingRooms) {

      List<ChattingContent> chattingContent = chattingRoom.getChattingContent();
      String content = null;
      LocalDateTime time = null;
      int readNotCount;

      // 읽지 않은 갯수
      readNotCount = chattingContentRepository
          .countBySenderNotAndIsReadAndChattingRoom(sender, false, chattingRoom);

      if (!chattingContent.isEmpty()) {
        content = chattingContent.get(chattingContent.size() - 1).getContent();
        time = chattingContent.get(chattingContent.size() - 1).getCreatedAt();
      }

      chattingRoomResponses
          .add(ChattingRoomAllResponse.from(chattingRoom, content, time, readNotCount));
    }   // for each 문 끝

    return chattingRoomResponses;
  }


  // 채팅방 단건조회
  @Transactional
  public ChatContentAllResponse getChattingRoomByIdAndMemberId(Long roomId, Long memberId) {

    // 채팅방 존재하는지 멤버가 존재하는지 검사
    Errand errand = chattingRoomFindById(roomId).getErrand();
    if (!validMemberInRoom(roomId, memberId)) {
      //채팅방 안에 멤버가 존재하지않는다.
      throw new IllegalArgumentException(ErrorCode.CHATTING_NOT_FOUND_MEMBER.getDescription());
    }
    List<ChattingContent> chattingContents = chattingContentRepository
        .findByChattingRoom_IdOrderByCreatedAtDesc(roomId);

    List<ChatContentResponse> chatContentResponses = new ArrayList<>();

    for (ChattingContent chattingContent : chattingContents) {
      chatContentResponses.add(ChatContentResponse.from(chattingContent));
    }


    return ChatContentAllResponse.from(roomId,errand.getTitle(), chatContentResponses);
  }

  // member가 있는지 유효성 검사
  public Member memberFindById(Long memberId) {
    return memberRepository.findById(memberId)
        .orElseThrow(() -> new IllegalArgumentException(ErrorCode.MEMBER_NOT_FOUND
            .getDescription()));
  }

  // 채팅방이 존재하는지 유효성 검사
  public ChattingRoom chattingRoomFindById(Long roomId) {
    return chattingRoomRepository.findById(roomId).orElseThrow(
        () -> new IllegalArgumentException(ErrorCode.CHATTING_NOT_FOUND.getDescription()));
  }

  // 채팅방 안에 멤버가 있는지 유효성 검사
  public boolean validMemberInRoom(Long roomId, Long memberId) {
    Member member = memberFindById(memberId);
    ChattingRoom room = chattingRoomFindById(roomId);
    return member == room.getSender() || member == room.getErrand().getErrander();
  }
}
