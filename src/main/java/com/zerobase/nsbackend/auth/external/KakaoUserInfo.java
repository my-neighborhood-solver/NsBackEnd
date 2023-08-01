package com.zerobase.nsbackend.auth.external;

import com.zerobase.nsbackend.auth.dto.KakaoUserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Component
public class KakaoUserInfo {
    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String USER_INFO_URI;

    public KakaoUserInfoResponse getUserInfo(String token) {
        String uri = USER_INFO_URI;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + token);
        HttpEntity request = new HttpEntity(httpHeaders);

        ResponseEntity<KakaoUserInfoResponse> responseEntity = restTemplate.exchange(
            uri,
            HttpMethod.GET,
            request,
            KakaoUserInfoResponse.class
        );

        return responseEntity.getBody();
    }
}
