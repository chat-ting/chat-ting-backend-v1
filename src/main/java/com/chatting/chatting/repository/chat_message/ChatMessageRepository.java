package com.chatting.chatting.repository.chat_message;


import com.chatting.chatting.global.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID>, ChatMessageRepositoryCustom {

    List<ChatMessage> findByRoomIdOrderByIdAsc(UUID roomId);

    Optional<ChatMessage> findTopByRoomIdOrderByIdDesc(UUID roomId);
}