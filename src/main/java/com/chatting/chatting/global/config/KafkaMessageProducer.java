package com.chatting.chatting.global.config;

import com.chatting.chatting.controller.dto.AuthUserInfo;
import com.chatting.chatting.controller.dto.ChatKafkaMessage;
import com.chatting.chatting.controller.dto.ChatMessagePayload;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
@RequiredArgsConstructor
public class KafkaMessageProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public Mono<Void> send(ChatMessagePayload payload, AuthUserInfo user) {
        return Mono.fromRunnable(() -> {
            try {
                String json = objectMapper.writeValueAsString(new ChatKafkaMessage(payload, user));
                kafkaTemplate.send("chat-message", json);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).subscribeOn(Schedulers.boundedElastic()).then();
    }
}