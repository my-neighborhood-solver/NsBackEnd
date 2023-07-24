package com.zerobase.nsbackend.member.web;

import com.zerobase.nsbackend.member.domain.Member;
import com.zerobase.nsbackend.member.security.TokenProvider;
import com.zerobase.nsbackend.member.service.AuthService;
import com.zerobase.nsbackend.member.dto.Auth.SignIn;
import com.zerobase.nsbackend.member.dto.Auth.SignInResponse;
import com.zerobase.nsbackend.member.dto.Auth.SignUp;
import com.zerobase.nsbackend.member.dto.Auth.SignUpResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final TokenProvider tokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponse> createMember(@RequestBody SignUp request){
        Member member = this.authService.register(request);
        SignUpResponse response = SignUpResponse.builder()
            .id(member.getId())
            .email(member.getEmail())
            .name(member.getName())
            .password(member.getPassword())
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

}
