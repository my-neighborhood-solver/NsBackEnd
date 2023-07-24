package com.zerobase.nsbackend.auth.utils;

import com.zerobase.nsbackend.auth.dto.KakaoUserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@Component
public class KakaoUserInfo {
    private final WebClient webClient;
    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String USER_INFO_URI;

    public KakaoUserInfoResponse getUserInfo(String token) {
        String uri = USER_INFO_URI;

        Flux<KakaoUserInfoResponse> response = webClient.get()
            .uri(uri)
            .header("Authorization", "Bearer " + token)
            .retrieve()
            .bodyToFlux(KakaoUserInfoResponse.class);

        return response.blockFirst();
    }
}
