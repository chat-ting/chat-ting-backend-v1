package com.chatting.chatting.controller;

import com.chatting.chatting.controller.dto.AuthUserInfo;
import com.chatting.chatting.controller.dto.ChatMessageDto;
import com.chatting.chatting.controller.dto.ChatRoomDto;
import com.chatting.chatting.controller.dto.CreateRoomRequest;
import com.chatting.chatting.global.entity.ChatRoom;
import com.chatting.chatting.global.service.RedisAuthService;
import com.chatting.chatting.global.util.StringUtil;
import com.chatting.chatting.service.ChatMessageService;
import com.chatting.chatting.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
@Controller
@RestController
@RequiredArgsConstructor
@RequestMapping("/chat-messages")
@Transactional(readOnly = true)
public class ChatMessageRestController {
    private final RedisAuthService redisAuthService; // token → userInfo 조회
    private final ChatMessageService chatMessageService;

    //기존 채팅 내용 조회
    @GetMapping
    public ResponseEntity<List<ChatMessageDto>> getChatMessages(@RequestHeader("Authorization") String authHeader,
                                                                @RequestParam("roomId") UUID roomId) {
        // 1. 토큰 파싱
        String token = StringUtil.extractTokenFromAuthHeader(authHeader);

        // 2. Redis에서 유저 조회
        AuthUserInfo user = redisAuthService.resolve(token)
                .orElseThrow(() -> new RuntimeException("인증 실패"));

        // 3. 채팅방 목록 조회
        return ResponseEntity.ok(chatMessageService.getMessages(roomId));
    }






}
