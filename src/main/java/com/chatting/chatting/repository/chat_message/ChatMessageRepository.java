package com.chatting.chatting.repository.chat_message;


import com.chatting.chatting.global.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID>, ChatMessageRepositoryCustom {

    List<ChatMessage> findByRoomIdOrderBySentAtAsc(UUID roomId);

}