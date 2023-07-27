package com.zerobase.nsbackend.auth.utils;

import com.zerobase.nsbackend.auth.dto.KakaoTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class KakaoTokenJsonData {
    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String TOKEN_URI;
    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String REDIRECT_URI;
    @Value("${spring.security.oauth2.client.registration.kakao.authorization-grant-type}")
    private String GRANT_TYPE;
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String CLIENT_ID;

    public KakaoTokenResponse getToken(String code) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity request = new HttpEntity(httpHeaders);

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(TOKEN_URI)
            .queryParam("grant_type", GRANT_TYPE)
            .queryParam("client_id", CLIENT_ID)
            .queryParam("redirect_uri", REDIRECT_URI)
            .queryParam("code", code);

        ResponseEntity<KakaoTokenResponse> responseEntity = restTemplate.exchange(
            uriComponentsBuilder.toUriString(),
            HttpMethod.POST,
            request,
            KakaoTokenResponse.class
        );

        return responseEntity.getBody();
    }
}
