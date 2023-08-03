package com.zerobase.nsbackend.auth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.zerobase.nsbackend.auth.dto.Auth.SignIn;
import com.zerobase.nsbackend.auth.dto.Auth.SignUp;
import com.zerobase.nsbackend.global.exceptionHandle.ErrorCode;
import com.zerobase.nsbackend.member.domain.Member;
import com.zerobase.nsbackend.member.repository.MemberRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class AuthServiceFailTest {
    @Mock
    private MemberRepository memberRepository;
    @InjectMocks
    private AuthService authService;
    @Mock
    private PasswordEncoder passwordEncoder;
    private final Member member = Member.builder()
        .nickname("test")
        .email("aa@naver.com")
        .password("encodePassword")
        .isSocialLogin(false)
        .build();

    @DisplayName("loadUser실패 - 일치하는 이메일 없음")
    @Test
    void loadUserByUsername_notFoundEmail() {
        //given
        given(memberRepository.findByEmail(any()))
            .willReturn(Optional.empty());
        //when
        UsernameNotFoundException exception = assertThrows(
            UsernameNotFoundException.class,
            () -> authService.loadUserByUsername("aa@naver.com"));
        //then
        assertEquals("couldn't find user ->aa@naver.com",exception.getMessage());
    }

    @DisplayName("회원가입 실패 - 동일 이메일 존재")
    @Test
    void register_existEmail() {
        //given
        SignUp signUpRequest = SignUp.builder()
            .nickname("test")
            .email("aa@naver.com")
            .password("123").build();
        given(memberRepository.existsByEmail(anyString()))
            .willReturn(true);
        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> authService.register(signUpRequest));
        //then
        assertEquals(ErrorCode.EMAIL_EXIST.getDescription(), exception.getMessage());
    }

    @DisplayName("로그인 싪패 - 존재하지 않은 유저")
    @Test
    void authenticate_NoExistUser() {
        //given
        SignIn signInRequest = SignIn.builder()
                .email("aa@naver.com")
                    .password("123").build();
        given(memberRepository.findByEmail(anyString()))
            .willReturn(Optional.empty());
        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> authService.authenticate(signInRequest));
        //then
        assertEquals(ErrorCode.NO_EXIST_EMAIL.getDescription(), exception.getMessage());
    }

    @DisplayName("로그인 싪패 - 소셜 로그인 유저")
    @Test
    void authenticate_IsSocialLogin() {
        //given
        Member social = Member.builder()
            .nickname("test")
            .email("aa@naver.com")
            .password("encodePassword")
            .isSocialLogin(true)
            .build();
        SignIn signInRequest = SignIn.builder()
            .email("aa@naver.com")
            .password("123").build();
        given(memberRepository.findByEmail(anyString()))
            .willReturn(Optional.ofNullable(social));
        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> authService.authenticate(signInRequest));
        //then
        assertEquals(ErrorCode.IS_SOCIAL_LOGIN.getDescription(), exception.getMessage());
    }
    @DisplayName("로그인 싪패 - 비밀번호가 일치하지 않음")
    @Test
    void authenticate_NoMatchPassword() {
        //given
        SignIn signInRequest = SignIn.builder()
            .email("aa@naver.com")
            .password("123").build();
        given(memberRepository.findByEmail(anyString()))
            .willReturn(Optional.ofNullable(member));
        given(passwordEncoder.matches(anyString(),anyString()))
            .willReturn(false);
        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> authService.authenticate(signInRequest));
        //then
        assertEquals(ErrorCode.NO_MATCH_PASSWORD.getDescription(), exception.getMessage());
    }

    @DisplayName("카카오로그인 - 유저가 없음")
    @Test
    void kakaoRegister_login_NotFoundUser() {
        //given
        given(memberRepository.findByEmail(any()))
            .willReturn(Optional.empty());
        given(memberRepository.existsByEmail(any()))
            .willReturn(true);
        //when
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
            () -> authService.kakaoRegister("aa@naver.com", "test"));
        //then
        assertEquals(ErrorCode.NOT_FOUND_USER.getDescription(),exception.getMessage());
    }

    @DisplayName("카카오로그인 - 소셜로그인이 아님")
    @Test
    void kakaoRegister_login_NoSocial() {
        //given
        Member social = Member.builder()
            .nickname("test")
            .email("aa@naver.com")
            .password("encodePassword")
            .isSocialLogin(false)
            .build();
        given(memberRepository.findByEmail(any()))
            .willReturn(Optional.ofNullable(social));
        given(memberRepository.existsByEmail(any()))
            .willReturn(true);
        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> authService.kakaoRegister("aa@naver.com", "test"));
        //then
        assertEquals(ErrorCode.IS_NOT_SOCAIL_USER.getDescription(),exception.getMessage());
    }
}