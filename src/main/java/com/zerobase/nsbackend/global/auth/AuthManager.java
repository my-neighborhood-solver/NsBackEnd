package com.zerobase.nsbackend.global.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthManager {
  Authentication getAuthentication();
  UserDetails getPrincipal();
  String getUsername();
}
