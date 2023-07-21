package com.zerobase.nsbackend.member.service;

import com.zerobase.nsbackend.member.domain.Member;
import com.zerobase.nsbackend.member.domain.repository.MemberRepository;
import com.zerobase.nsbackend.member.dto.Auth;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
class AuthServiceTest {

    @Autowired
    AuthService authService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("회원가입 성공")
    void successRegister() {
        //given
        Auth.SignUp signupRequest = Auth.SignUp.builder()
                .name("wnstj")
                .email("sfds@naver.com")
                .password("sdf123").build();

        //when
        Member register = authService.register(signupRequest);

        //then
        Optional<Member> member = memberRepository.findByEmail("sfds@naver.com");
        assertEquals(member.get().getName(),signupRequest.getName());
    }
    @Test
    @DisplayName("이메일 중복 - 회원가입 실패")
    void duplicationEmailRegister() {
        //given
        Auth.SignUp signupRequest = Auth.SignUp.builder()
                .name("wnstj")
                .email("sfds@naver.com")
                .password("sdf123").build();

        //when
        Member register = authService.register(signupRequest);

        //then
        assertThrows(IllegalArgumentException.class, () -> authService.register(signupRequest));
    }

    @Test
    void authenticate() {
    }
}