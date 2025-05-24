package com.chatting.chatting.controller;

import com.chatting.chatting.controller.dto.AuthUserInfo;
import com.chatting.chatting.controller.dto.ChatMessagePayload;
import com.chatting.chatting.global.config.KafkaMessageProducer;
import com.chatting.chatting.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;
    private final KafkaMessageProducer kafkaMessageProducer;
    @MessageMapping("/send")
    public Mono<Void> handleMessage(@Payload ChatMessagePayload payload,
                                    Message<?> message) {

        Map<String, Object> sessionAttrs = (Map<String, Object>)
                message.getHeaders().get(SimpMessageHeaderAccessor.SESSION_ATTRIBUTES);
        AuthUserInfo user = (AuthUserInfo) sessionAttrs.get("user");

        return kafkaMessageProducer.send(payload, user).then();
    }
}