package com.zerobase.nsbackend.member.domain.service;

import com.zerobase.nsbackend.member.domain.Member;
import com.zerobase.nsbackend.member.domain.repository.MemberRepository;
import com.zerobase.nsbackend.member.dto.Auth.SignIn;
import com.zerobase.nsbackend.member.dto.Auth.SignUp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public Member register(SignUp signupReqest){
        boolean exists =  this.memberRepository.existsByEmail(signupReqest.getEmail());
        if(exists){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        signupReqest.setPassword(this.passwordEncoder.encode(signupReqest.getPassword()));
        Member mem = this.memberRepository.save(signupReqest.toEntity());
        return mem;
    }

    public Member authenticate(SignIn signinReqest){
        Member members = this.memberRepository.findByEmail(signinReqest.getEmail())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
        if(!this.passwordEncoder.matches(signinReqest.getPassword(), members.getPassword())){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return members;
    }
}
