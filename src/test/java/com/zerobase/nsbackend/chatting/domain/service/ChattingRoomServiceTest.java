package com.zerobase.nsbackend.chatting.domain.service;

import static com.zerobase.nsbackend.chatting.type.ChattingRoomCreateStatus.CHATTING_ROOM_CREATE_EXIST;
import static com.zerobase.nsbackend.chatting.type.ChattingRoomCreateStatus.CHATTING_ROOM_CREATE_SUCCESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.zerobase.nsbackend.chatting.domain.entity.ChattingRoom;
import com.zerobase.nsbackend.chatting.domain.repository.ChattingRoomRepository;
import com.zerobase.nsbackend.chatting.dto.ChattingRoomCreateResponse;
import com.zerobase.nsbackend.errand.domain.service.ErrandService;
import com.zerobase.nsbackend.errand.domain.entity.Errand;
import com.zerobase.nsbackend.member.domain.Member;
import com.zerobase.nsbackend.member.repository.MemberRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class ChattingRoomServiceTest {

  @Mock
  private ChattingRoomRepository chattingRoomRepository;

  @Mock
  private MemberRepository memberRepository;

  @Mock
  private ErrandService errandService;


  @InjectMocks
  private ChattingRoomService chattingRoomService;


  Member member1;
  Member member2;

  Errand errand1;
  Errand errand2;

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
}
