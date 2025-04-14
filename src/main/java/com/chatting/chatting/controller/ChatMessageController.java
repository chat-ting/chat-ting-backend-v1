package com.chatting.chatting.controller;

import com.chatting.chatting.controller.dto.AuthUserInfo;
import com.chatting.chatting.controller.dto.ChatMessagePayload;
import com.chatting.chatting.global.entity.ChatMessage;
import com.chatting.chatting.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Map;
@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/send") // 클라이언트가 /chat/send로 보냄
    public void handleMessage(@Payload ChatMessagePayload payload,
                              Message<?> message) {

        Map<String, Object> sessionAttrs = (Map<String,Object>)
                message.getHeaders().get(SimpMessageHeaderAccessor.SESSION_ATTRIBUTES);
        AuthUserInfo user = (AuthUserInfo) sessionAttrs.get("user");

        log.info("✉️ {}({})의 메시지: {}", user.getUsername(), user.getMemberId(), payload.content());
        ChatMessage saved = chatMessageService.saveMessage(
                payload.roomId(),
                user.getMemberId(),
                payload.content()
        );

        // 구독자에게 전송 (예: /topic/chat/방ID)
        messagingTemplate.convertAndSend(
                "/topic/chat/" + payload.roomId(),
                saved
        );
    }
}