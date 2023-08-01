package com.zerobase.nsbackend.chatting.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    // "/topic"으로 시작하는 대상(destination)에 대해 메시지를 브로드캐스팅합니다.
    config.enableSimpleBroker("/topic");
    // "/app"으로 시작하는 대상(destination)에 대해 메시지를 처리합니다.
    config.setApplicationDestinationPrefixes("/app");
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    // "/ws"로 웹소켓 연결을 열고 SockJS를 통해 클라이언트와 통신합니다.
    registry.addEndpoint("/ws").withSockJS();
  }
}
