package com.zerobase.nsbackend.member.service;

import com.zerobase.nsbackend.errand.domain.entity.Review;
import com.zerobase.nsbackend.errand.domain.repository.ReviewRepository;
import com.zerobase.nsbackend.global.exceptionHandle.ErrorCode;
import com.zerobase.nsbackend.global.fileUpload.StoreFileToAWS;
import com.zerobase.nsbackend.global.fileUpload.UploadFile;
import com.zerobase.nsbackend.member.domain.Member;
import com.zerobase.nsbackend.member.domain.MemberAddress;
import com.zerobase.nsbackend.member.dto.GetReviewResponse;
import com.zerobase.nsbackend.member.dto.GetUserResponse;
import com.zerobase.nsbackend.member.dto.HashtagResponse;
import com.zerobase.nsbackend.member.dto.PutUserAddressRequest;
import com.zerobase.nsbackend.member.dto.PutUserNicknameRequest;
import com.zerobase.nsbackend.member.repository.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final StoreFileToAWS storeFileToAWS;
    private final ReviewRepository reviewRepository;

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
    public Member updateUserImg(MultipartFile request, String email){
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException(ErrorCode.MEMBER_NOT_FOUND.getDescription()));
        UploadFile uploadFile = storeFileToAWS.storeFile(request);
        String url = storeFileToAWS.responseFileUrl(uploadFile);
        member.updateUserImg(url);
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

    @Transactional
    public HashtagResponse getHashtag(String email){
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException(ErrorCode.MEMBER_NOT_FOUND.getDescription()));
        return HashtagResponse.of(member.getHashtags());
    }

    @Transactional
    public HashtagResponse updateHashtag(String email, String content){
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException(ErrorCode.MEMBER_NOT_FOUND.getDescription()));
        if(member.getHashtags().size() >= 5){
            throw new IllegalArgumentException(ErrorCode.HASHTAG_IS_FULL.getDescription());
        }
        member.addHashtag(content);
        return HashtagResponse.of(member.getHashtags());
    }

    @Transactional
    public HashtagResponse deleteHashtag(String email, String content){
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException(ErrorCode.MEMBER_NOT_FOUND.getDescription()));
        if(!member.existHashtag(content)){
            throw new IllegalArgumentException(ErrorCode.NO_EXIST_HASHTAG.getDescription());
        }
        member.deleteHashtag(content);
        return HashtagResponse.of(member.getHashtags());
    }

    @Transactional
    public List<GetReviewResponse> getMyReview(String email){
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException(ErrorCode.MEMBER_NOT_FOUND.getDescription()));
        List<Review> reviews = reviewRepository.findAllByReviewee(member);
        List<GetReviewResponse> responseList = new ArrayList<>();
        for(Review review : reviews){
            GetReviewResponse build = GetReviewResponse.builder()
                .errandId(review.getErrand().getId())
                .errandTitle(review.getErrand().getTitle())
                .grade(review.getGrade().getDescription())
                .comment(review.getComment()).build();
            responseList.add(build);
        }
        return responseList;
    }

}
