package com.chatting.chatting.global.test;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestDataCleaner {

    private final RedisTemplate<String, String> redisTemplate;

    @PreDestroy
    public void cleanup() {
        String token = "test-jwt-token";
        redisTemplate.delete(token);
        System.out.println("🧹 Redis 테스트 토큰 삭제 완료");
    }
}
