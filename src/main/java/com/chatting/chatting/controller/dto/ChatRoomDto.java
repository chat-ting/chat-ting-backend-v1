package com.chatting.chatting.controller.dto;

import com.chatting.chatting.global.entity.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ChatRoomDto {
    String roomId;
    String title;

    public static ChatRoomDto from(ChatRoom chatRoom) {
        return new ChatRoomDto(chatRoom.getId().toString(), chatRoom.getName());
    }
}
