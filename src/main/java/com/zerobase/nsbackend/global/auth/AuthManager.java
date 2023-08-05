package com.zerobase.nsbackend.global.auth;

import com.zerobase.nsbackend.member.domain.Member;
import org.springframework.security.core.Authentication;

public interface AuthManager {
  Authentication getAuthentication();
  Member getPrincipal();
  String getUsername();
}
