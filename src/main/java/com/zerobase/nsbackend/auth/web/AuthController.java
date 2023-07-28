package com.zerobase.nsbackend.auth.web;

import com.zerobase.nsbackend.member.domain.Member;
import com.zerobase.nsbackend.auth.dto.Auth.SignIn;
import com.zerobase.nsbackend.auth.dto.Auth.SignInResponse;
import com.zerobase.nsbackend.auth.dto.Auth.SignUp;
import com.zerobase.nsbackend.auth.dto.Auth.SignUpResponse;
import com.zerobase.nsbackend.auth.dto.KakaoTokenResponse;
import com.zerobase.nsbackend.auth.dto.KakaoUserInfoResponse;
import com.zerobase.nsbackend.auth.security.TokenProvider;
import com.zerobase.nsbackend.auth.service.AuthService;
import com.zerobase.nsbackend.auth.utils.KakaoTokenJsonData;
import com.zerobase.nsbackend.auth.utils.KakaoUserInfo;
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
            .nickname(member.getNickname())
            .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signin")
    public ResponseEntity<SignInResponse> logInMember(@RequestBody SignIn request){
        Member member = this.authService.authenticate(request);
        String token = this.tokenProvider.generateToken(member.getEmail(), member.getRole());
        SignInResponse response = SignInResponse.builder()
            .id(member.getId())
            .email(member.getEmail())
            .nickname(member.getNickname())
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
    public ResponseEntity<SignInResponse> kakaoOauth(@RequestParam("code") String code) {
        KakaoTokenResponse kakaoTokenResponse = kakaoTokenJsonData.getToken(code);
        KakaoUserInfoResponse userInfo = kakaoUserInfo.getUserInfo(kakaoTokenResponse.getAccess_token());
        String name = userInfo.getKakao_account().getProfile().getNickname();
        Member member = this.authService.kakaoRegister(userInfo.getKakao_account().getEmail(),
            name);
        String token = this.tokenProvider.generateToken(member.getEmail(),member.getRole());
        SignInResponse response = SignInResponse.builder()
            .id(member.getId())
            .email(member.getEmail())
            .nickname(member.getNickname())
            .token(token)
            .build();
        return ResponseEntity.ok(response);
    }

}
