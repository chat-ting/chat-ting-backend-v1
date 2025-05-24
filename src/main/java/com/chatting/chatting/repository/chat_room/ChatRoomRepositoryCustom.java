package com.chatting.chatting.repository.chat_room;

import com.chatting.chatting.global.entity.ChatRoom;
import reactor.core.publisher.Flux;

import java.util.List;

public interface ChatRoomRepositoryCustom {
    Flux<ChatRoom> findChatRoomsByMemberId(long memberId);
}
