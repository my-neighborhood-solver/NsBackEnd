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
    return SecurityContextHolder.getContext().getAuthentication();
  }

  @Override
  public UserDetails getPrincipal() {
    return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }

  @Override
  public String getUsername() {
    return getAuthentication().getName();
  }
}
