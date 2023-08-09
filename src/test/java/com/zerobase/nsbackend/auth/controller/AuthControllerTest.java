package com.zerobase.nsbackend.auth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.nsbackend.auth.dto.Auth.SignIn;
import com.zerobase.nsbackend.auth.dto.Auth.SignUp;
import com.zerobase.nsbackend.auth.dto.KakaoAccount;
import com.zerobase.nsbackend.auth.dto.KakaoProfile;
import com.zerobase.nsbackend.auth.dto.KakaoTokenResponse;
import com.zerobase.nsbackend.auth.dto.KakaoUserInfoResponse;
import com.zerobase.nsbackend.auth.external.KakaoTokenFeign;
import com.zerobase.nsbackend.auth.external.KakaoUserFeign;
import com.zerobase.nsbackend.auth.security.TokenProvider;
import com.zerobase.nsbackend.auth.service.AuthService;
import com.zerobase.nsbackend.member.domain.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @MockBean
    private AuthService authService;
    @MockBean
    private TokenProvider tokenProvider;
    @MockBean
    private KakaoTokenFeign KakaoTokenJsonData;
    @MockBean
    private KakaoUserFeign kakaoUserInfo;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private final Member member = Member.builder()
        .id(1L)
        .nickname("test")
        .email("aa@naver.com")
        .password("encodePassword")
        .isSocialLogin(false)
        .build();

    @Test
    void createMember() throws Exception {
        given(authService.register(any()))
            .willReturn(member);
        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                    new SignUp(member.getNickname(),member.getEmail(),"123")
                )))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.nickname").value("test"))
            .andExpect(jsonPath("$.email").value("aa@naver.com"));
    }

    @Test
    void logInMember() throws Exception {
        given(authService.authenticate(any()))
            .willReturn(member);
        given(tokenProvider.generateToken(any(),any()))
            .willReturn("generateToken");
        mockMvc.perform(post("/auth/signin")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(
                new SignIn(member.getEmail(),member.getPassword())
                )))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.nickname").value(member.getNickname()))
            .andExpect(jsonPath("$.email").value(member.getEmail()))
            .andExpect(jsonPath("$.token").value("generateToken"));
    }

    @Test
    void kakaoOauth() throws Exception {
        KakaoAccount kakaoAccount = KakaoAccount.builder()
            .email("bb@naver.com")
            .profile(KakaoProfile.builder().nickname("test2").build())
            .build();
        given(KakaoTokenJsonData.call(any(),any(),any(),any()))
            .willReturn(KakaoTokenResponse.builder().build());
        given(kakaoUserInfo.call(any()))
            .willReturn(KakaoUserInfoResponse.builder()
                .kakao_account(kakaoAccount)
                .build());
        given(authService.kakaoRegister(kakaoAccount.getEmail()
            ,kakaoAccount.getProfile().getNickname()))
            .willReturn(Member.builder()
                .id(2L)
                .nickname(kakaoAccount.getProfile().getNickname())
                .email(kakaoAccount.getEmail())
                .password("encodePassword")
                .build());
        given(tokenProvider.generateToken(any(),any()))
            .willReturn("generateToken");

        mockMvc.perform(get("/auth/kakao/callback?code=1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(2))
            .andExpect(jsonPath("$.email").value("bb@naver.com"))
            .andExpect(jsonPath("$.nickname").value("test2"))
            .andExpect(jsonPath("$.token").value("generateToken"));
    }
}