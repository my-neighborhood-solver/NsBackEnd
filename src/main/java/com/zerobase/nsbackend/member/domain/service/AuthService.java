package com.zerobase.nsbackend.member.domain.service;

import com.zerobase.nsbackend.member.domain.Members;
import com.zerobase.nsbackend.member.domain.repository.MemberRepository;
import com.zerobase.nsbackend.member.dto.Auth;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class AuthService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Long userId = Long.parseLong(id);
        return this.memberRepository.findById(userId)
            .orElseThrow(() -> new UsernameNotFoundException("couldn't find user ->" + id));
    }

    public Members register(Auth.SignUp member){
        boolean exists =  this.memberRepository.existsByEmail(member.getEmail());
        if(exists){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"email is exists");
        }
        member.setPassword(this.passwordEncoder.encode(member.getPassword()));
        Members mem = this.memberRepository.save(member.toEntity());
        return mem;
    }
}
