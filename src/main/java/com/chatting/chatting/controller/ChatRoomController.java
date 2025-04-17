package com.chatting.chatting.controller;

import com.chatting.chatting.controller.dto.AuthUserInfo;
import com.chatting.chatting.controller.dto.ChatRoomDto;
import com.chatting.chatting.controller.dto.CreateRoomRequest;
import com.chatting.chatting.global.entity.ChatRoom;
import com.chatting.chatting.global.service.RedisAuthService;
import com.chatting.chatting.global.util.StringUtil;
import com.chatting.chatting.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat-rooms")
@Transactional(readOnly = true)
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final RedisAuthService redisAuthService; // token → userInfo 조회

    @PostMapping
    @Transactional
    public ResponseEntity<Map<String,UUID>> createRoom(@RequestHeader("Authorization") String authHeader,
                                           @RequestBody CreateRoomRequest request) {
        // 1. 토큰 파싱
        String token = StringUtil.extractTokenFromAuthHeader(authHeader);

        // 2. Redis에서 유저 조회
        AuthUserInfo user = redisAuthService.resolve(token)
                .orElseThrow(() -> new RuntimeException("인증 실패"));

        // 3. 채팅방 생성
        ChatRoom room = chatRoomService.createRoom(request.name()==null?"new chat": request.name(), user.getMemberId());

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("roomId", room.getId()));
    }



    @GetMapping
    public ResponseEntity<List<ChatRoomDto>> getMemberChatRooms(@RequestHeader("Authorization") String authHeader) {
        // 1. 토큰 파싱
        String token = StringUtil.extractTokenFromAuthHeader(authHeader);

        // 2. Redis에서 유저 조회
        AuthUserInfo user = redisAuthService.resolve(token)
                .orElseThrow(() -> new RuntimeException("인증 실패"));


        // 3. 채팅방 목록 조회
        return ResponseEntity.ok(chatRoomService.getChatRoomsByMemberId(user.getMemberId()));
    }
}
