package com.zerobase.nsbackend.member.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

import com.zerobase.nsbackend.auth.service.AuthService;
import com.zerobase.nsbackend.member.domain.Member;
import com.zerobase.nsbackend.member.domain.repository.MemberRepository;
import com.zerobase.nsbackend.auth.dto.Auth;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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
            .nickname("wnstj")
            .email("sfds@naver.com")
            .password("sdf123").build();

        //when
        Member register = authService.register(signupRequest);

        //then
        Optional<Member> member = memberRepository.findByEmail("sfds@naver.com");
        assertEquals(member.get().getNickname(),signupRequest.getNickname());
    }
    @Test
    @DisplayName("이메일 중복 - 회원가입 실패")
    void duplicationEmailRegister() {
        //given
        Auth.SignUp signupRequest = Auth.SignUp.builder()
                .nickname("wnstj")
                .email("sfds@naver.com")
                .password("sdf123").build();

        //when
        authService.register(signupRequest);

        //then
        assertThrows(IllegalArgumentException.class, () -> authService.register(signupRequest));
    }

    @Test
    @DisplayName("로그인 성공")
    void SuccessAuthenticate() {
        //given
        Auth.SignUp signupRequest = Auth.SignUp.builder()
            .nickname("wnstj")
            .email("sfds@naver.com")
            .password("sdf123").build();

        Member member = authService.register(signupRequest);

        Auth.SignIn signInRequest = Auth.SignIn.builder()
            .email(member.getEmail())
            .password(member.getPassword())
            .build();

        //when
        Member authenticate = authService.authenticate(signInRequest);

        assertEquals(authenticate.getId(), member.getId());
    }

    @Test
    @DisplayName("회원정보가 없음 - 로그인 실패")
    void FailAuthenticateNoExistData() {
        //given
        Auth.SignIn signInRequest = Auth.SignIn.builder()
            .email("sfdsdf@naver.com")
            .password("sdfsdf123")
            .build();

        assertThrows(IllegalArgumentException.class, () -> authService.authenticate(signInRequest));
    }

    @Test
    @DisplayName("비밀번호 매칭 실패 - 로그인 실패")
    void FailAuthenticateNoMatchPassword() {
        //given
        Auth.SignUp signupRequest = Auth.SignUp.builder()
            .nickname("wnstj")
            .email("sfds@naver.com")
            .password("sdf123").build();

        Member member = authService.register(signupRequest);

        Auth.SignIn signInRequest = Auth.SignIn.builder()
            .email(member.getEmail())
            .password("111222333")
            .build();

        assertThrows(IllegalArgumentException.class, () -> authService.authenticate(signInRequest));
    }
}