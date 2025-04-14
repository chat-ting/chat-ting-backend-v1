package com.chatting.chatting.controller;

import com.chatting.chatting.controller.dto.AuthUserInfo;
import com.chatting.chatting.controller.dto.CreateRoomRequest;
import com.chatting.chatting.global.entity.ChatRoom;
import com.chatting.chatting.global.service.RedisAuthService;
import com.chatting.chatting.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat-rooms")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final RedisAuthService redisAuthService; // token → userInfo 조회

    @PostMapping
    public ResponseEntity<UUID> createRoom(@RequestHeader("Authorization") String authHeader,
                                           @RequestBody CreateRoomRequest request) {
        // 1. 토큰 파싱
        String token = extractToken(authHeader);

        // 2. Redis에서 유저 조회
        AuthUserInfo user = redisAuthService.resolve(token)
                .orElseThrow(() -> new RuntimeException("인증 실패"));

        // 3. 채팅방 생성
        ChatRoom room = chatRoomService.createRoom(request.name(), user.getMemberId());

        return ResponseEntity.ok(room.getId());
    }

    private String extractToken(String header) {
        if (header == null || !header.startsWith("Bearer ")) throw new RuntimeException("토큰 없음");
        return header.substring(7);
    }
}
