package com.chatting.chatting.global.config;

import com.chatting.chatting.global.interceptor.JwtHandshakeInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final JwtHandshakeInterceptor jwtHandshakeInterceptor;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // 서버 -> 클라
        // 클 → 서버 전송 prefix
        config.setApplicationDestinationPrefixes("/chat");// chat/xxx로 메시지 들어오면 @MessageMapping("/xxx") 찾음
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws") // 클라이언트 연결 엔드포인트
                .setAllowedOriginPatterns("*")
                .addInterceptors(jwtHandshakeInterceptor) // 🔥 여기!
                .withSockJS(); // fallback 지원
        registry.addEndpoint("/ws-native")
                .setAllowedOriginPatterns("*")
                .addInterceptors(jwtHandshakeInterceptor) // 🔥 여기!
        ;                 // JMeter용 (raw ws)
    }


}