package com.zerobase.nsbackend.member.service;

import com.zerobase.nsbackend.global.exceptionHandle.ErrorCode;
import com.zerobase.nsbackend.member.domain.Member;
import com.zerobase.nsbackend.member.domain.MemberAddress;
import com.zerobase.nsbackend.member.dto.GetUserResponse;
import com.zerobase.nsbackend.member.dto.PutProfileImgRequest;
import com.zerobase.nsbackend.member.dto.PutUserAddressRequest;
import com.zerobase.nsbackend.member.dto.PutUserNicknameRequest;
import com.zerobase.nsbackend.member.repository.MemberAddressRepository;
import com.zerobase.nsbackend.member.repository.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberAddressRepository memberAddressRepository;
    private final MemberRepository memberRepository;

    public GetUserResponse getUserInfo(String email){
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException(ErrorCode.MEMBER_NOT_FOUND.getDescription()));
        return GetUserResponse.builder()
            .email(member.getEmail())
            .nickname(member.getNickname())
            .profileImage(member.getProfileImage())
            .latitude(member.getMemberAddress().getLatitude())
            .longitude(member.getMemberAddress().getLongitude())
            .streetNameAddress(member.getMemberAddress().getStreetNameAddress())
            .build();
    }

    @Transactional
    public Member updateUserNickname(PutUserNicknameRequest request, String email){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.MEMBER_NOT_FOUND.getDescription()));
        member.updateUserNickname(request.getNickname());
        return member;
    }

    @Transactional
    public Member updateUserImg(PutProfileImgRequest request, String email){
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException(ErrorCode.MEMBER_NOT_FOUND.getDescription()));
        member.updateUserImg(request.getImg());
        return member;
    }

    @Transactional
    public MemberAddress updateUserAddress(PutUserAddressRequest request, String email){
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException(ErrorCode.MEMBER_NOT_FOUND.getDescription()));
        MemberAddress memberAddress = member.getMemberAddress();
        memberAddress.updateUserAddress(request.getLatitude(),
            request.getLongitude(), request.getStreetNameAddress());
        return memberAddress;
    }

    @Transactional
    public Member deleteUser(String email){
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException(ErrorCode.MEMBER_NOT_FOUND.getDescription()));
        member.deleteUser();
        return member;
    }

}
