package com.zerobase.nsbackend.global.auth;

import com.zerobase.nsbackend.member.domain.Member;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthManagerImpl implements AuthManager {

  @Override
  public Authentication getAuthentication() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication;
  }

  @Override
  public Member getPrincipal() {
    Member principal = (Member) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();
    return principal;
  }

  @Override
  public String getUsername() {
    return getPrincipal().getEmail();
  }
}
