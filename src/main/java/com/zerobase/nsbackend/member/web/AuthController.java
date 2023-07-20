package com.zerobase.nsbackend.member.web;

import com.zerobase.nsbackend.member.domain.Members;
import com.zerobase.nsbackend.member.domain.service.AuthService;
import com.zerobase.nsbackend.member.dto.Auth.SignUp;
import com.zerobase.nsbackend.member.dto.Auth.SignUpResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> createMember(@RequestBody SignUp request){
        Members members = this.authService.register(request);
        SignUpResponse response = SignUpResponse.builder()
            .id(members.getId())
            .email(members.getEmail())
            .name(members.getName())
            .password(members.getPassword())
            .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
