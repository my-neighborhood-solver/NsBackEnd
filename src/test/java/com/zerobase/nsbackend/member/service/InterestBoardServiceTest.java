package com.zerobase.nsbackend.member.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.zerobase.nsbackend.errand.domain.entity.Errand;
import com.zerobase.nsbackend.errand.domain.repository.ErrandRepository;
import com.zerobase.nsbackend.global.exceptionHandle.ErrorCode;
import com.zerobase.nsbackend.member.domain.InterestBoard;
import com.zerobase.nsbackend.member.domain.Member;
import com.zerobase.nsbackend.member.dto.InterestBoardResponse;
import com.zerobase.nsbackend.member.repository.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InterestBoardServiceTest {

    @InjectMocks
    private InterestBoardService interestBoardService;
    @Mock
    private ErrandRepository errandRepository;
    @Mock
    private MemberRepository memberRepository;

    private final Member member = Member.builder()
        .id(1L)
        .nickname("test")
        .email("aa@naver.com")
        .password("encodePassword")
        .interestBoards(new ArrayList<>())
        .build();
    private final Errand errand = Errand.builder()
        .id(1L)
        .errander(member)
        .title("title").build();
    private final InterestBoard interestBoard = new InterestBoard(member,errand);


    @Test
    void addInterestBoard() {
        //given
        given(errandRepository.findById(any()))
            .willReturn(Optional.ofNullable(errand));
        List<InterestBoardResponse> list = new ArrayList<>();
        assert errand != null;
        InterestBoardResponse build = InterestBoardResponse.builder()
            .errandId(errand.getId())
            .errandTitle(errand.getTitle())
            .build();
        list.add(build);
        //when
        List<InterestBoardResponse> responses = this.interestBoardService.addInterestBoard(
            1L, member);
        //then
        assertEquals(list.get(0).getErrandId(),responses.get(0).getErrandId());
        assertEquals(list.get(0).getErrandTitle(),responses.get(0).getErrandTitle());
    }
    @DisplayName("관심글 추가 실패 - 해당하는 의뢰가 존재하지 않음")
    @Test
    void addInterestBoard_notExistErrand() {
        //given
        given(errandRepository.findById(any()))
            .willReturn(Optional.empty());
        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> interestBoardService.addInterestBoard(1L, member));
        //then
        assertEquals(ErrorCode.ERRAND_NOT_FOUND.getDescription(),exception.getMessage());
    }
    @DisplayName("관심글 추가 실패 - 이미 관심글로 등록된 의뢰")
    @Test
    void addInterestBoard_existInterestBoard() {
        //given
        given(errandRepository.findById(any()))
            .willReturn(Optional.ofNullable(errand));
        List<InterestBoard> list = new ArrayList<>();
        list.add(interestBoard);
        Member build = Member.builder()
            .id(1L)
            .nickname("test")
            .email("aa@naver.com")
            .password("encodePassword")
            .interestBoards(list)
            .build();
        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> interestBoardService.addInterestBoard(1L, build));
        //then
        assertEquals(ErrorCode.EXIST_INTEREST_BOARD.getDescription(),exception.getMessage());
    }

    @Test
    void getAllInterestBoard() {
        //given
        List<InterestBoard> list = new ArrayList<>();
        list.add(interestBoard);
        Member build = Member.builder()
            .id(1L)
            .nickname("test")
            .email("aa@naver.com")
            .password("encodePassword")
            .interestBoards(list)
            .build();
        //when
        List<InterestBoardResponse> allInterestBoard = this.interestBoardService.getAllInterestBoard(
            build);
        assertEquals(errand.getId(),allInterestBoard.get(0).getErrandId());
        assertEquals(errand.getTitle(),allInterestBoard.get(0).getErrandTitle());
    }

    @Test
    void deleteInterestBoard() {
        //given
        given(errandRepository.findById(any()))
            .willReturn(Optional.ofNullable(errand));
        List<InterestBoard> list = new ArrayList<>();
        list.add(interestBoard);
        Member build = Member.builder()
            .id(1L)
            .nickname("test")
            .email("aa@naver.com")
            .password("encodePassword")
            .interestBoards(list)
            .build();
        //when
        List<InterestBoardResponse> responses = this.interestBoardService.deleteInterestBoard(
            1L, build);
        //then
        assertEquals(new ArrayList<>(),responses);
    }

    @DisplayName("관심글 삭제 실패 - 해당하는 의뢰가 존재하지 않음")
    @Test
    void deleteInterestBoard_notExistErrand() {
        //given
        given(errandRepository.findById(any()))
            .willReturn(Optional.empty());
        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> interestBoardService.deleteInterestBoard(1L, member));
        //then
        assertEquals(ErrorCode.ERRAND_NOT_FOUND.getDescription(), exception.getMessage());
    }
    @DisplayName("관심글 삭제 실패 - 관심글로 등록하지 않은 의뢰")
    @Test
    void deleteInterestBoard_existInterestBoard() {
        //given
        given(errandRepository.findById(any()))
            .willReturn(Optional.ofNullable(errand));
        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> interestBoardService.deleteInterestBoard(1L, member));
        //then
        assertEquals(ErrorCode.NO_EXIST_INTEREST_BOARD.getDescription(),exception.getMessage());
    }
}