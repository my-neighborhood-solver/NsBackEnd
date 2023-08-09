package com.zerobase.nsbackend.auth.external;

import com.zerobase.nsbackend.auth.dto.KakaoUserInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "KakaoUserFeign"
    , url = "${spring.security.oauth2.client.provider.kakao.user-info-uri}")
public interface KakaoUserFeign {
    @GetMapping
    KakaoUserInfoResponse call(
        @RequestHeader(value = "Authorization") String token
    );
}
