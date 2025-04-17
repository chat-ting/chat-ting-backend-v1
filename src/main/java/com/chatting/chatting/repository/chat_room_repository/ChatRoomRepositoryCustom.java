package com.chatting.chatting.repository.chat_room_repository;

import com.chatting.chatting.global.entity.ChatRoom;

import java.util.List;

public interface ChatRoomRepositoryCustom {
    List<ChatRoom> findChatRoomsByMemberId(long memberId);
}
