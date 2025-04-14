package com.chatting.chatting.global.service;

import com.chatting.chatting.controller.dto.AuthUserInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RedisAuthService {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public Optional<AuthUserInfo> resolve(String token) {
        String json = redisTemplate.opsForValue().get(token);
        if (json == null) return Optional.empty();

        try {
            return Optional.of(objectMapper.readValue(json, AuthUserInfo.class));
        } catch (JsonProcessingException e) {
            return Optional.empty();
        }
    }
}