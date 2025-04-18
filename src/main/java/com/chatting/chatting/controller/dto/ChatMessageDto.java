package com.chatting.chatting.controller.dto;

import com.chatting.chatting.global.entity.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class ChatMessageDto {
    String id;
    Long senderId;
    String senderName;
    String content;
    long sentAt;


    public static ChatMessageDto from(ChatMessage mesage) {
        return ChatMessageDto.builder()
                .id(mesage.getId().toString())
                .senderId(mesage.getSenderId())
                .content(mesage.getContent())
                .sentAt(mesage.getSentAt().toInstant().toEpochMilli())
                .senderName(mesage.getMemberName())
                .build();
    }
}
