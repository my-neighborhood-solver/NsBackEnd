package com.zerobase.nsbackend.member.web;

import com.zerobase.nsbackend.member.domain.Member;
import com.zerobase.nsbackend.member.dto.Auth.SignIn;
import com.zerobase.nsbackend.member.dto.Auth.SignInResponse;
import com.zerobase.nsbackend.member.dto.Auth.SignUp;
import com.zerobase.nsbackend.member.dto.Auth.SignUpResponse;
import com.zerobase.nsbackend.member.dto.KakaoTokenResponse;
import com.zerobase.nsbackend.member.dto.KakaoUserInfoResponse;
import com.zerobase.nsbackend.member.security.TokenProvider;
import com.zerobase.nsbackend.member.service.AuthService;
import com.zerobase.nsbackend.member.utils.KakaoTokenJsonData;
import com.zerobase.nsbackend.member.utils.KakaoUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@Slf4j
@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final TokenProvider tokenProvider;
    private final KakaoTokenJsonData kakaoTokenJsonData;
    private final KakaoUserInfo kakaoUserInfo;

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponse> createMember(@RequestBody SignUp request){
        Member member = this.authService.register(request);
        SignUpResponse response = SignUpResponse.builder()
            .id(member.getId())
            .email(member.getEmail())
            .name(member.getName())
            .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signin")
    public ResponseEntity<SignInResponse> logInMember(@RequestBody SignIn request){
        Member member = this.authService.authenticate(request);
        String token = this.tokenProvider.generateToken(member.getEmail());
        SignInResponse response = SignInResponse.builder()
            .id(member.getId())
            .email(member.getEmail())
            .name(member.getName())
            .token(token)
            .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/social/kakao")
    public RedirectView kakaoLogin (){
        RedirectView redirectView = new RedirectView();
        String url = authService.kakaoLogin();
        redirectView.setUrl(url);
        return redirectView;
    }

    @GetMapping("/kakao/callback")
    @ResponseBody
    public ResponseEntity<SignUpResponse> kakaoOauth(@RequestParam("code") String code) {
        log.info("인가 코드를 이용하여 토큰을 받습니다. : "+code);
        KakaoTokenResponse kakaoTokenResponse = kakaoTokenJsonData.getToken(code);
        log.info("토큰에 대한 정보입니다. : {}",kakaoTokenResponse);
        KakaoUserInfoResponse userInfo = kakaoUserInfo.getUserInfo(kakaoTokenResponse.getAccess_token());
        log.info("회원 정보 입니다.{}",userInfo);
        String name = userInfo.getKakao_account().getProfile().getNickname();
        Member member = this.authService.kakaoRegister(userInfo.getKakao_account().getEmail(),
            name);
        SignUpResponse response = SignUpResponse.builder()
            .id(member.getId())
            .email(member.getEmail())
            .name(member.getName())
            .build();
        return ResponseEntity.ok(response);
    }

}
