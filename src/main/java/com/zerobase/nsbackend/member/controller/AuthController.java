package com.zerobase.nsbackend.member.controller;

import com.zerobase.nsbackend.member.dto.Auth;
import com.zerobase.nsbackend.member.type.SuccessCode;
import com.zerobase.nsbackend.member.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
public class AuthController {
    @PostMapping("/signup")
    public ResponseEntity<?> createMember(@RequestBody Auth.SignUp request){
        return null;
    }
}
