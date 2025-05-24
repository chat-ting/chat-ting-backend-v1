package com.chatting.chatting.repository.chat_message;


import com.chatting.chatting.global.entity.ChatMessage;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ChatMessageRepository extends ReactiveCrudRepository<ChatMessage, UUID> {

    Flux<ChatMessage> findByRoomIdOrderByIdAsc(UUID roomId);

    Mono<ChatMessage> findTopByRoomIdOrderByIdDesc(UUID roomId);
}