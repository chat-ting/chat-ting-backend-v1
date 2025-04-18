package com.chatting.chatting.repository.chat_room;

import com.chatting.chatting.global.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, UUID>, ChatRoomRepositoryCustom {
    // Custom query methods can be defined here if needed
}
