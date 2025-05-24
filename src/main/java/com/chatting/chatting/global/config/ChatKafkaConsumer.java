package com.chatting.chatting.global.config;

import com.chatting.chatting.controller.dto.AuthUserInfo;
import com.chatting.chatting.controller.dto.ChatKafkaMessage;
import com.chatting.chatting.controller.dto.ChatMessagePayload;
import com.chatting.chatting.service.ChatMessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatKafkaConsumer {

    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "chat-message", groupId = "chat-group")
    public void listen(String message) {
        try {
            ChatKafkaMessage kafkaMessage = objectMapper.readValue(message, ChatKafkaMessage.class);
            ChatMessagePayload payload = kafkaMessage.payload();
            AuthUserInfo user = kafkaMessage.user();

            chatMessageService.saveMessage(
                    payload.roomId(),
                    user.getMemberId(),
                    user.getUsername(),
                    payload.content()
            ).subscribe(savedMessage -> {
                messagingTemplate.convertAndSend(
                        "/topic/chat/" + payload.roomId(),
                        savedMessage
                );
            });
        } catch (Exception e) {
            log.error("❌ Kafka consumer 처리 실패", e);
        }
    }
}
