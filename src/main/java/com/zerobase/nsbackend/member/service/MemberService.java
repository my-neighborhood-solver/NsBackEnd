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
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
            .build();
        MemberAddress memberAddress = memberAddressRepository.findAllByMember(member)
            .orElseThrow(() -> new NoSuchElementException(ErrorCode.NO_EXIST_DATA.getDescription()));
        build.setLongitude(memberAddress.getLongitude());
        build.setLatitude(memberAddress.getLatitude());
        build.setStreetNameAddress(memberAddress.getStreetNameAddress());
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
        boolean exists = memberAddressRepository.existsByMember(member);
        if(exists){
            MemberAddress memberAddress = memberAddressRepository.findAllByMember(member)
                .orElseThrow(() -> new NoSuchElementException(ErrorCode.NO_EXIST_DATA.getDescription()));
            memberAddress.updateUserAddress(request.getLatitude(),
                request.getLongitude(), request.getStreetNameAddress());
            memberAddressRepository.save(memberAddress);
            return memberAddress;
        }else{
            MemberAddress memberAddress = memberAddressRepository.save(MemberAddress.builder()
                .member(member)
                .streetNameAddress(request.getStreetNameAddress())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .permission(true)
                .build());
            return memberAddress;
        }
    }

    public Member deleteUser(Member member){
        member.deleteUser();
        memberRepository.save(member);
        return member;
    }

}
