package com.zerobase.nsbackend.auth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.zerobase.nsbackend.auth.dto.Auth.SignIn;
import com.zerobase.nsbackend.auth.dto.Auth.SignUp;
import com.zerobase.nsbackend.member.domain.Member;
import com.zerobase.nsbackend.member.repository.MemberRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class AuthServiceSuccessTest {
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

    @Test
    void loadUserByUsername() {
        //given
        given(memberRepository.findByEmail("aa@naver.com"))
            .willReturn(Optional.ofNullable(member));
        //when
        Member username = (Member) authService.loadUserByUsername("aa@naver.com");
        //then
        assert member != null;
        assertEquals(member.getNickname(), username.getNickname());
        assertEquals(member.getEmail(), username.getEmail());
        assertEquals(member.getPassword(), username.getPassword());
    }

    @Test
    void register() {
        //given
        SignUp signUpRequest = SignUp.builder()
            .nickname("test")
            .email("aa@naver.com")
            .password("123").build();
        ArgumentCaptor<Member> captor = ArgumentCaptor.forClass(Member.class);
        given(memberRepository.save(any()))
            .willReturn(member);
        given(passwordEncoder.encode(anyString()))
            .willReturn("encodePassword");
        given(memberRepository.existsByEmail(anyString()))
            .willReturn(false);
        //when
        authService.register(signUpRequest);
        //then
        verify(memberRepository, times(1))
            .save(captor.capture());
        Member savedMember = captor.getValue();
        assertEquals(signUpRequest.getNickname(), savedMember.getNickname());
        assertEquals(signUpRequest.getEmail(), savedMember.getEmail());
        assertEquals(signUpRequest.getPassword(), savedMember.getPassword());
    }

    @Test
    void authenticate() {
        //given
        SignIn signInRequest = SignIn.builder()
                .email("aa@naver.com")
                    .password("123").build();
        given(memberRepository.findByEmail(any()))
            .willReturn(Optional.ofNullable(member));
        given(passwordEncoder.matches("123","encodePassword"))
            .willReturn(true);
        //when
        Member authenticate = authService.authenticate(signInRequest);
        //then
        assert member != null;
        assertEquals(member.getEmail(),authenticate.getEmail());
        assertEquals(member.getPassword(),authenticate.getPassword());
        assertEquals(member.getNickname(),authenticate.getNickname());
    }

    @Test
    void kakaoLogin() {
        //given
        ReflectionTestUtils.setField(authService,"secretKey"," ");
        ReflectionTestUtils.setField(authService,"redirectUri"," ");
        String uri = "https://kauth.kakao.com/oauth/authorize?client_id="
            + " "
            + "&redirect_uri="
            + " "
            + "&response_type=code";
        //when
        String kakaoLogin = authService.kakaoLogin();
        //then
        assertEquals(uri,kakaoLogin);
    }

    @Test
    void kakaoRegister_login() {
        //given
        Member social = Member.builder()
            .nickname("test")
            .email("aa@naver.com")
            .password("encodePassword")
            .isSocialLogin(true)
            .build();
        given(memberRepository.findByEmail(any()))
            .willReturn(Optional.ofNullable(social));
        given(memberRepository.existsByEmail(any()))
            .willReturn(true);
        //when
        Member kakaoRegister = authService.kakaoRegister("aa@naver.com", "test");
        //then
        assert social != null;
        assertEquals(social.getNickname(),kakaoRegister.getNickname());
        assertEquals(social.getPassword(), kakaoRegister.getPassword());
        assertEquals(social.getEmail(),kakaoRegister.getEmail());
    }
    @Test
    void kakaoRegister_register() {
        //given
        Member social = Member.builder()
            .nickname("test")
            .email("aa@naver.com")
            .password("encodePassword")
            .isSocialLogin(true)
            .build();
        given(memberRepository.existsByEmail(any()))
            .willReturn(false);
        given(memberRepository.save(any()))
            .willReturn(social);
        //when
        Member kakaoRegister = authService.kakaoRegister("aa@naver.com", "test");
        //theb
        assertEquals(social.getNickname(),kakaoRegister.getNickname());
        assertEquals(social.getEmail(),kakaoRegister.getEmail());
    }
}