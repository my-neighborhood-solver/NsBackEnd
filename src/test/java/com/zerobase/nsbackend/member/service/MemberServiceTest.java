package com.zerobase.nsbackend.member.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.zerobase.nsbackend.member.domain.Member;
import com.zerobase.nsbackend.member.domain.MemberAddress;
import com.zerobase.nsbackend.member.dto.GetUserResponse;
import com.zerobase.nsbackend.member.dto.PutProfileImgRequest;
import com.zerobase.nsbackend.member.dto.PutUserAddressRequest;
import com.zerobase.nsbackend.member.dto.PutUserNicknameRequest;
import com.zerobase.nsbackend.member.repository.MemberAddressRepository;
import com.zerobase.nsbackend.member.repository.MemberRepository;
import java.util.Optional;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;
    @Mock
    MemberAddressRepository memberAddressRepository;
    @Mock
    MemberRepository memberRepository;
    private final MemberAddress memberAddress = MemberAddress.builder()
        .latitude(0f)
        .longitude(1f)
        .streetNameAddress("street").build();
    private final Member member = Member.builder()
        .nickname("test")
        .email("aa@naver.com")
        .password("encodePassword")
        .memberAddress(memberAddress)
        .profileImage(null)
        .isSocialLogin(false)
        .build();

    @Test
    void getUserInfo() {
        //given
        given(memberRepository.findByEmail(any()))
            .willReturn(Optional.ofNullable(member));
        //when
        GetUserResponse userInfo = memberService.getUserInfo("aa@naver.com");
        //then
        assertEquals("aa@naver.com",userInfo.getEmail());
        assertEquals(1,userInfo.getLongitude());
        assertEquals(0,userInfo.getLatitude());
        assertEquals("street",userInfo.getStreetNameAddress());
    }

    @Test
    void updateUserNickname() {
        //given
        given(memberRepository.findByEmail(any()))
            .willReturn(Optional.ofNullable(member));
        PutUserNicknameRequest request = new PutUserNicknameRequest();
        request.setNickname("update");
        //when
        Member updated = memberService.updateUserNickname(request, "aa@naver.com");
        //then
        assertEquals("update",updated.getNickname());
        assertEquals(member.getEmail(),updated.getEmail());
    }

    @Test
    void updateUserImg() {
        //given
        given(memberRepository.findByEmail(any()))
            .willReturn(Optional.ofNullable(member));
        PutProfileImgRequest request = new PutProfileImgRequest();
        request.setImg("set");
        //when
        Member updated = memberService.updateUserImg(request, member.getEmail());
        //then
        assertEquals("set",updated.getProfileImage());
        assertEquals(member.getEmail(),updated.getEmail());
    }

    @Test
    void updateUserAddress() {
        //given
        given(memberRepository.findByEmail(any()))
            .willReturn(Optional.ofNullable(member));
        PutUserAddressRequest request = PutUserAddressRequest.builder()
            .latitude(12.123f)
            .longitude(32.123f)
            .streetNameAddress("updateStreet").build();
        //when
        MemberAddress updated = memberService.updateUserAddress(request, member.getEmail());
        //then
        assertEquals(12.123f, updated.getLatitude());
        assertEquals(32.123f, updated.getLongitude());
        assertEquals("updateStreet",updated.getStreetNameAddress());
    }

    @Test
    void deleteUser() {
        //given
        given(memberRepository.findByEmail(any()))
            .willReturn(Optional.ofNullable(member));
        //when
        Member deleteUser = memberService.deleteUser(member.getEmail());
        //then
        assertTrue(deleteUser.isDeleted());
    }
}