package com.zerobase.nsbackend.member.service;

import com.zerobase.nsbackend.global.exceptionHandle.ErrorCode;
import com.zerobase.nsbackend.member.domain.Member;
import com.zerobase.nsbackend.member.domain.repository.MemberRepository;
import com.zerobase.nsbackend.member.dto.Auth.SignIn;
import com.zerobase.nsbackend.member.dto.Auth.SignUp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Long userId = Long.parseLong(id);
        return this.memberRepository.findById(userId)
            .orElseThrow(() -> new UsernameNotFoundException("couldn't find user ->" + id));
    }

    public Member register(SignUp signupRequest){
        boolean exists =  this.memberRepository.existsByEmail(signupRequest.getEmail());
        if(exists){
            throw new IllegalArgumentException(ErrorCode.EMAIL_EXIST.getDescription());
        }
        signupRequest.setPassword(this.passwordEncoder.encode(signupRequest.getPassword()));
        Member mem = this.memberRepository.save(signupRequest.toEntity());
        return mem;
    }

    public Member authenticate(SignIn signinRequest){
        Member members = this.memberRepository.findByEmail(signinRequest.getEmail())
            .orElseThrow(() -> new IllegalArgumentException(ErrorCode.NO_EXIST_EMAIL.getDescription()));
        if(!this.passwordEncoder.matches(signinRequest.getPassword(), members.getPassword())){
            throw new IllegalArgumentException(ErrorCode.NO_MATCH_PASSWORD.getDescription());
        }
        return members;
    }
}
