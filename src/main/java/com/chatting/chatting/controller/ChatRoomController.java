package com.chatting.chatting.controller;

import com.chatting.chatting.controller.dto.AuthUserInfo;
import com.chatting.chatting.controller.dto.ChatRoomDto;
import com.chatting.chatting.controller.dto.CreateRoomRequest;
import com.chatting.chatting.global.service.RedisAuthService;
import com.chatting.chatting.global.util.StringUtil;
import com.chatting.chatting.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat-rooms")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final RedisAuthService redisAuthService; // token → userInfo 조회

    @PostMapping
    public Mono<Map<String,UUID>> createRoom(@RequestHeader("Authorization") String authHeader,
                                             @RequestBody CreateRoomRequest request) {
        // 1. 토큰 파싱
        String token = StringUtil.extractTokenFromAuthHeader(authHeader);

        // 2. Redis에서 유저 조회
        AuthUserInfo user = redisAuthService.resolve(token)
                .orElseThrow(() -> new RuntimeException("인증 실패"));

        // 3. 채팅방 생성
        return chatRoomService.createRoom(request.name()==null?"new chat": request.name(), user.getMemberId())
                .map(room -> Map.of(
                        "roomId", room.getId()
                ));
    }



    @GetMapping
    public Flux<ChatRoomDto> getMemberChatRooms(@RequestHeader("Authorization") String authHeader) {
        // 1. 토큰 파싱
        String token = StringUtil.extractTokenFromAuthHeader(authHeader);

        // 2. Redis에서 유저 조회
        AuthUserInfo user = redisAuthService.resolve(token)
                .orElseThrow(() -> new RuntimeException("인증 실패"));


        // 3. 채팅방 목록 조회
        return chatRoomService.getChatRoomsByMemberId(user.getMemberId());
    }
}
