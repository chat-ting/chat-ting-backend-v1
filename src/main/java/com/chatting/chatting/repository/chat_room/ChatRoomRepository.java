package com.chatting.chatting.repository.chat_room;

import com.chatting.chatting.global.entity.ChatRoom;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface ChatRoomRepository extends ReactiveCrudRepository<ChatRoom, UUID>, ChatRoomRepositoryCustom {
    // Custom query methods can be defined here if needed
}
