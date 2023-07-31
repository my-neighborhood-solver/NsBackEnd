package com.zerobase.nsbackend.auth.service;

import com.zerobase.nsbackend.global.exceptionHandle.ErrorCode;
import com.zerobase.nsbackend.member.domain.Member;
import com.zerobase.nsbackend.member.domain.MemberAddress;
import com.zerobase.nsbackend.member.repository.MemberAddressRepository;
import com.zerobase.nsbackend.member.repository.MemberRepository;
import com.zerobase.nsbackend.auth.dto.Auth.SignIn;
import com.zerobase.nsbackend.auth.dto.Auth.SignUp;
import com.zerobase.nsbackend.member.type.Authority;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@PropertySource("classpath:application-oauth.yml")
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberAddressRepository memberAddressRepository;
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String secretKey;
    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return this.memberRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("couldn't find user ->" + email));
    }

    public Member register(SignUp signupRequest){
        boolean exists =  this.memberRepository.existsByEmail(signupRequest.getEmail());
        if(exists){
            throw new IllegalArgumentException(ErrorCode.EMAIL_EXIST.getDescription());
        }
        signupRequest.setPassword(this.passwordEncoder.encode(signupRequest.getPassword()));
        Member mem = this.memberRepository.save(signupRequest.toEntity());
        this.memberAddressRepository.save(MemberAddress.builder().member(mem).build());
        return mem;
    }

    public Member authenticate(SignIn signinRequest){
        Member members = this.memberRepository.findByEmail(signinRequest.getEmail())
            .orElseThrow(() -> new IllegalArgumentException(ErrorCode.NO_EXIST_EMAIL.getDescription()));
        if(members.getIsSocialLogin()){
            throw new IllegalArgumentException(ErrorCode.IS_SOCIAL_LOGIN.getDescription());
        }
        if(!this.passwordEncoder.matches(signinRequest.getPassword(), members.getPassword())){
            throw new IllegalArgumentException(ErrorCode.NO_MATCH_PASSWORD.getDescription());
        }
        return members;
    }
    public String kakaoLogin(){
        return "https://kauth.kakao.com/oauth/authorize?client_id="
            + secretKey
            + "&redirect_uri="
            + redirectUri
            + "&response_type=code";
    }

    public Member kakaoRegister(String email, String nickname){
        boolean exists =  this.memberRepository.existsByEmail(email);
        if(exists){
            Member byEmail = this.memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(ErrorCode.NOT_FOUND_USER.getDescription()));
            if(byEmail.getIsSocialLogin()){
                return byEmail;
            }else{
                throw new IllegalArgumentException(ErrorCode.IS_NOT_SOCAIL_USER.getDescription());
            }
        }
        Member member = this.memberRepository.save(Member.builder()
            .nickname(nickname)
            .email(email)
            .authority(Authority.ROLE_USER)
            .isSocialLogin(true)
            .isDeleted(false)
            .build());
        return member;
    }
}
