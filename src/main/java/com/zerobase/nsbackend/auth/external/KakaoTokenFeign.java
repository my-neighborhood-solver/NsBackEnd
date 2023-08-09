package com.zerobase.nsbackend.auth.external;

import com.zerobase.nsbackend.auth.dto.KakaoTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "KakaoTokenFeign"
    , url = "${spring.security.oauth2.client.provider.kakao.token-uri}")
public interface KakaoTokenFeign {
    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    KakaoTokenResponse call(
        @RequestParam String grant_type,
        @RequestParam String client_id,
        @RequestParam String redirect_uri,
        @RequestParam String code);
}
