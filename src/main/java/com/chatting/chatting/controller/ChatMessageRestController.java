package com.chatting.chatting.controller;

import com.chatting.chatting.controller.dto.AuthUserInfo;
import com.chatting.chatting.controller.dto.ChatRoomDto;
import com.chatting.chatting.controller.dto.CreateRoomRequest;
import com.chatting.chatting.global.entity.ChatRoom;
import com.chatting.chatting.global.service.RedisAuthService;
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
@RequestMapping("/chat-messages")
@Transactional(readOnly = true)
public class ChatMessageRestController {
    private final RedisAuthService redisAuthService; // token → userInfo 조회





}
