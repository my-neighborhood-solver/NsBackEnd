package com.zerobase.nsbackend.chatting.domain.service;

import static com.zerobase.nsbackend.chatting.type.ChattingRoomCreateStatus.CHATTING_ROOM_CREATE_EXIST;
import static com.zerobase.nsbackend.chatting.type.ChattingRoomCreateStatus.CHATTING_ROOM_CREATE_SUCCESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.zerobase.nsbackend.chatting.domain.entity.ChattingContent;
import com.zerobase.nsbackend.chatting.domain.entity.ChattingRoom;
import com.zerobase.nsbackend.chatting.domain.repository.ChattingContentRepository;
import com.zerobase.nsbackend.chatting.domain.repository.ChattingRoomRepository;
import com.zerobase.nsbackend.chatting.dto.ChatContentResponse;
import com.zerobase.nsbackend.chatting.dto.ChattingRoomAllResponse;
import com.zerobase.nsbackend.chatting.dto.ChattingRoomCreateResponse;
import com.zerobase.nsbackend.errand.domain.ErrandService;
import com.zerobase.nsbackend.errand.domain.entity.Errand;
import com.zerobase.nsbackend.member.domain.Member;
import com.zerobase.nsbackend.member.repository.MemberRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class ChattingRoomServiceTest {

  @Mock
  private ChattingRoomRepository chattingRoomRepository;

  @Mock
  private MemberRepository memberRepository;

  @Mock
  private ChattingContentRepository chattingContentRepository;

  @Mock
  private ErrandService errandService;


  @InjectMocks
  private ChattingRoomService chattingRoomService;


  Member member1;
  Member member2;

  Errand errand1;
  Errand errand2;

  ChattingRoom chattingRoom1;
  ChattingRoom chattingRoom2;

  ChattingContent chattingContent1;
  ChattingContent chattingContent2;

  @BeforeEach
  void setUp() {
    member1 = Member.builder()
        .id(1L)
        .email("testUser1@tsetexample.com")
        .password("1234")
        .nickname("강아지")
        .build();
    member2 = Member.builder()
        .id(2L)
        .email("testUser2@tsetexample.com")
        .password("1234")
        .nickname("호랑이")
        .build();

    errand1 = Errand.builder()
        .id(101L)
        .errander(member1)
        .title("안녕 아녕")
        .content("안녕 안녕하세요")
        .build();
    errand2 = Errand.builder()
        .id(102L)
        .errander(member2)
        .title("ㅎㅇ")
        .content("ㅎㅇ ㅎㅇ")
        .build();

    chattingRoom1 = ChattingRoom.builder()
        .id(1L)
        .errand(errand1)
        .sender(member2)
        .build();

    chattingRoom2 = ChattingRoom.builder()
        .id(2L)
        .errand(errand2)
        .sender(member1)
        .build();

    chattingContent1 = ChattingContent.builder()
        .content("밥먹자")
        .chattingRoom(chattingRoom1)
        .sender(member1)
        .isRead(true)
        .build();
    chattingContent2 = ChattingContent.builder()
        .content("배고프다")
        .chattingRoom(chattingRoom1)
        .sender(member2)
        .isRead(false)
        .build();
  }

  @Test
  @DisplayName("채팅방 생성 성공")
  void testCreateChattingRoom1() {
    // given

    // when
    // 가짜 회원 데이터를 리포지토리에서 반환하도록 설정
    when(errandService.getErrand(1L)).thenReturn(errand1);
    when(memberRepository.findById(2L)).thenReturn(Optional.of(member2));
    when(chattingRoomRepository.findByErrandAndSender(errand1, member2))
        .thenReturn(Optional.empty());
    // 채팅방 생성을 나타내는 새로운 ChattingRoom 객체 생성
    ChattingRoom chattingRoom1 = ChattingRoom.builder()
        .id(1001L)
        .errand(errand1)
        .sender(member2)
        .build();
    when(chattingRoomRepository.save(any(ChattingRoom.class))).thenReturn(chattingRoom1);
    // then

    ChattingRoomCreateResponse createResponse = chattingRoomService.createChattingRoom(1L, 2L);

    assertThat(createResponse).isNotNull();
    assertThat(createResponse.getChattingRoomId()).isEqualTo(1001L);
    assertThat(createResponse.getErrandId()).isEqualTo(101L);
    assertThat(createResponse.getSenderId()).isEqualTo(2L);
    assertThat(createResponse.getDescription()).isEqualTo(CHATTING_ROOM_CREATE_SUCCESS);
  }

  @Test
  @DisplayName("채팅방 생성 실패 -> 이미 채팅방 존재")
  void testCreateChattingRoom2() {
    // given
    // 이미 존재하는 채팅방 객체 생성
    ChattingRoom ChattingRoom1 = ChattingRoom.builder()
        .id(1001L)
        .errand(errand1)
        .sender(member2)
        .build();

    // when
    // 가짜 회원 데이터를 리포지토리에서 반환하도록 설정
    when(errandService.getErrand(1L)).thenReturn(errand1);
    when(memberRepository.findById(2L)).thenReturn(Optional.of(member2));
    // 존재하는 채팅방 객체를 반환하도록 설정
    when(chattingRoomRepository.findByErrandAndSender(errand1, member2))
        .thenReturn(Optional.of(ChattingRoom1));

    // then
    ChattingRoomCreateResponse createResponse = chattingRoomService.createChattingRoom(1L, 2L);

    assertThat(createResponse).isNotNull();
    assertThat(createResponse.getChattingRoomId()).isEqualTo(1001L);
    assertThat(createResponse.getErrandId()).isEqualTo(101L);
    assertThat(createResponse.getSenderId()).isEqualTo(2L);
    assertThat(createResponse.getDescription()).isEqualTo(CHATTING_ROOM_CREATE_EXIST);

  }

  @Test
  @DisplayName("채팅방 전체조회")
  void testGetChattingRoomsByMemberId() {
    Long memberId = 1L;

    // Mocking
    when(errandService.getErrand(1L)).thenReturn(errand1);
    when(memberRepository.findById(2L)).thenReturn(Optional.of(member2));
    when(chattingRoomRepository.findByErrandAndSender(errand1, member2))
        .thenReturn(Optional.empty());
    when(chattingRoomRepository.save(any(ChattingRoom.class))).thenReturn(chattingRoom1);

    when(memberRepository.findById(memberId)).thenReturn(Optional.of(member1));
    when(chattingRoomRepository.findByErrand_Errander_IdOrSenderId(memberId, memberId))
        .thenReturn(Arrays.asList(chattingRoom1, chattingRoom2));

    when(chattingContentRepository
        .countBySenderNotAndIsReadAndChattingRoom(member1, false, chattingRoom1)).thenReturn(5);
    when(chattingContentRepository
        .countBySenderNotAndIsReadAndChattingRoom(member1, false, chattingRoom2)).thenReturn(7);
    // 테스트 실행
    List<ChattingRoomAllResponse> response = chattingRoomService
        .getChattingRoomsByMemberId(memberId);

    // 결과 검증
    assertThat(response.size()).isEqualTo(2);
    assertThat(response.get(0).getReadCount()).isEqualTo(5);
    assertThat(response.get(1).getReadCount()).isEqualTo(7);

    // Mock 검증
    Mockito.verify(memberRepository, Mockito.times(1)).findById(memberId);
    Mockito.verify(chattingRoomRepository, Mockito.times(1))
        .findByErrand_Errander_IdOrSenderId(memberId, memberId);
    Mockito.verify(chattingContentRepository, Mockito.times(1))
        .countBySenderNotAndIsReadAndChattingRoom(member1, false, chattingRoom1);
    Mockito.verify(chattingContentRepository, Mockito.times(1))
        .countBySenderNotAndIsReadAndChattingRoom(member1, false, chattingRoom2);
  }

  @Test
  @DisplayName("채팅방 전체조회 실패")
  void testGetChattingRoomsByMemberIdFail() {
    Long memberId = 1L;

    // Mocking
    when(errandService.getErrand(1L)).thenReturn(errand1);
    when(memberRepository.findById(2L)).thenReturn(Optional.of(member2));
    when(chattingRoomRepository.findByErrandAndSender(errand1, member2))
        .thenReturn(Optional.empty());

    when(memberRepository.findById(memberId)).thenReturn(Optional.of(member1));

    // 테스트 실행 및 예외 검증
    assertThrows(IllegalArgumentException.class,
        () -> chattingRoomService.getChattingRoomsByMemberId(memberId), "채팅방이 존재하지 않습니다.");

    // Mock 검증
    Mockito.verify(memberRepository, Mockito.times(1)).findById(memberId);
    Mockito.verify(chattingRoomRepository, Mockito.times(1))
        .findByErrand_Errander_IdOrSenderId(memberId, memberId);
  }

  @Test
  @DisplayName("채팅방 단건 조회 성공")
  void testGetChattingRoomByIdAndMemberId_Success() {

    Long roomId = 1L;

    when(chattingRoomRepository.findById(eq(roomId))).thenReturn(Optional.of(chattingRoom1));

    when(memberRepository.findById(member1.getId())).thenReturn(Optional.of(member1));
    when(memberRepository.findById(member2.getId())).thenReturn(Optional.of(member2));

    when(chattingContentRepository.findByChattingRoom_IdOrderByCreatedAtDesc(roomId))
        .thenReturn(Arrays.asList(chattingContent1, chattingContent2));

    // 테스트 실행
    List<ChatContentResponse> response = chattingRoomService
        .getChattingRoomByIdAndMemberId(roomId, member1.getId());

    // 결과 검증
    assertThat(response.size()).isEqualTo(2);
    assertThat(response.get(0).getNickName()).isEqualTo(member1.getNickname());
    assertThat(response.get(1).getNickName()).isEqualTo(member2.getNickname());
    assertThat(response.get(0).getContent()).isEqualTo("밥먹자");
    assertThat(response.get(1).getContent()).isEqualTo("배고프다");

    // Mock 검증
    Mockito.verify(chattingRoomRepository, Mockito.times(1)).findById(eq(roomId));
    Mockito.verify(memberRepository, Mockito.times(1)).findById(member1.getId());
    Mockito.verify(chattingContentRepository, Mockito.times(1))
        .findByChattingRoom_IdOrderByCreatedAtDesc(roomId);
  }
}