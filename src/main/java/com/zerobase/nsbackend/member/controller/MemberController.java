package com.zerobase.nsbackend.member.controller;

import com.zerobase.nsbackend.member.domain.Member;
import com.zerobase.nsbackend.member.dto.GetUserResponse;
import com.zerobase.nsbackend.member.dto.Interests.interestBoardResponse;
import com.zerobase.nsbackend.member.dto.PutProfileImgRequest;
import com.zerobase.nsbackend.member.dto.PutUserAddressRequest;
import com.zerobase.nsbackend.member.dto.PutUserNicknameRequest;
import com.zerobase.nsbackend.member.service.InterestBoardService;
import com.zerobase.nsbackend.member.service.MemberService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/users")
@PreAuthorize("hasRole('USER')")
@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;
    private final InterestBoardService interestBoardService;

    @GetMapping()
    public ResponseEntity<GetUserResponse> getUserInfo(@AuthenticationPrincipal Member member){
        GetUserResponse userInfo = memberService.getUserInfo(member);
        return ResponseEntity.ok(userInfo);
    }
    @PutMapping("/profileimg")
    public ResponseEntity<GetUserResponse> putProfileImg(@RequestBody @Valid PutProfileImgRequest request
        , @AuthenticationPrincipal Member member){
        memberService.updateUserImg(request, member);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/nickname")
    public ResponseEntity<GetUserResponse> putUserNickname(@RequestBody @Valid PutUserNicknameRequest request
    , @AuthenticationPrincipal Member member){
        memberService.updateUserNickname(request, member);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/address")
    public ResponseEntity<GetUserResponse> putUserAddress(@RequestBody @Valid PutUserAddressRequest request
    , @AuthenticationPrincipal Member member){
        memberService.updateUserAddress(request, member);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping()
    public ResponseEntity<GetUserResponse> deleteUserInfo(@AuthenticationPrincipal Member member){
        memberService.deleteUser(member);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/interests")
    public ResponseEntity<List<interestBoardResponse>> getInterestBoard(
        @AuthenticationPrincipal Member member){
        List<interestBoardResponse> allInterestBoard = this.interestBoardService.getAllInterestBoard(
            member);
        return ResponseEntity.ok(allInterestBoard);
    }

    @PutMapping("/interests/{id}")
    public ResponseEntity<List<interestBoardResponse>> addInterestBoard(@PathVariable Long id
        , @AuthenticationPrincipal Member member){
        List<interestBoardResponse> interestBoardResponseList = this.interestBoardService.addInterestBoard(
            id, member);
        return ResponseEntity.ok(interestBoardResponseList);
    }

    @DeleteMapping("/interests/{id}")
    public ResponseEntity<List<interestBoardResponse>> deleteInterestBoard(@PathVariable Long id
        , @AuthenticationPrincipal Member member){
        List<interestBoardResponse> interestBoardResponseList = this.interestBoardService.deleteInterestBoard(
            id, member);
        return ResponseEntity.ok(interestBoardResponseList);
    }

}
