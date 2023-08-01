package com.zerobase.nsbackend.member.service;

import com.zerobase.nsbackend.member.domain.Member;
import com.zerobase.nsbackend.member.domain.MemberAddress;
import com.zerobase.nsbackend.member.dto.GetUserResponse;
import com.zerobase.nsbackend.member.dto.PutProfileImgRequest;
import com.zerobase.nsbackend.member.dto.PutUserAddressRequest;
import com.zerobase.nsbackend.member.dto.PutUserNicknameRequest;
import com.zerobase.nsbackend.member.repository.MemberAddressRepository;
import com.zerobase.nsbackend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberAddressRepository memberAddressRepository;
    private final MemberRepository memberRepository;

    public GetUserResponse getUserInfo(Member member){
        GetUserResponse build = GetUserResponse.builder()
            .email(member.getEmail())
            .nickname(member.getNickname())
            .profileImage(member.getProfileImage())
            .latitude(member.getMemberAddress().getLatitude())
            .longitude(member.getMemberAddress().getLongitude())
            .streetNameAddress(member.getMemberAddress().getStreetNameAddress())
            .build();
        return build;
    }

    public Member updateUserNickname(PutUserNicknameRequest request, Member member){
        member.updateUserNickname(request.getNickname());
        memberRepository.save(member);
        return member;
    }

    public Member updateUserImg(PutProfileImgRequest request, Member member){
        member.updateUserImg(request.getImg());
        memberRepository.save(member);
        return member;
    }

    public MemberAddress updateUserAddress(PutUserAddressRequest request, Member member){
        MemberAddress memberAddress = member.getMemberAddress();
        memberAddress.updateUserAddress(request.getLatitude(),
            request.getLongitude(), request.getStreetNameAddress());
        memberAddressRepository.save(memberAddress);
        return memberAddress;
    }

    public Member deleteUser(Member member){
        member.deleteUser();
        memberRepository.save(member);
        return member;
    }

}
