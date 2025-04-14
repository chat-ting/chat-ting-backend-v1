package com.chatting.chatting.global.test;


import com.chatting.chatting.controller.dto.AuthUserInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * ✅ 스프링 부트가 시작될 때 자동으로 실행되는 초기화 클래스
 * - 테스트용 JWT 토큰과 사용자 ID를 Redis에 미리 저장
 * - WebSocket 연결이나 API 테스트 시 사용할 수 있음
 */
@Component
@RequiredArgsConstructor
public class TestDataInitializer implements ApplicationRunner {

    @Value("${auth.token-ttl-minutes}")
    private long tokenTtlMinutes;

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper(); // Jackson

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String token = "test-jwt-token";

        AuthUserInfo testUser = AuthUserInfo.builder()
                .memberId(-1L)
                .username("테스트유저")
                .build();

        // 객체를 JSON 문자열로 직렬화
        String json = objectMapper.writeValueAsString(testUser);

        // Redis에 저장
        redisTemplate.opsForValue().set(token, json, Duration.ofMinutes(tokenTtlMinutes));

        System.out.println("✅ Redis에 테스트용 JWT 등록 완료: " + token + " → " + json);
    }
}
