package com.chatting.chatting.repository;

import com.chatting.chatting.global.entity.ChatRoomMember;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

public interface ChatRoomMemberRepository extends ReactiveCrudRepository<ChatRoomMember, UUID> {
    Flux<ChatRoomMember> findByRoomIdAndUserId(UUID roomId, Long userId);

    Flux<ChatRoomMember> findByRoomId(UUID roomId);
    Mono<Boolean> existsByRoomIdAndUserId(UUID roomId, Long userId);
}