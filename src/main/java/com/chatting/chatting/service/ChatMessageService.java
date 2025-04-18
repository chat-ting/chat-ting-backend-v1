package com.chatting.chatting.service;

import com.chatting.chatting.controller.dto.ChatMessageDto;
import com.chatting.chatting.exception.NotFoundException;
import com.chatting.chatting.global.entity.ChatMessage;
import com.chatting.chatting.global.entity.ChatRoom;
import com.chatting.chatting.repository.chat_message.ChatMessageRepository;
import com.chatting.chatting.repository.chat_room.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository messageRepository;

    public ChatMessage saveMessage(UUID roomId, Long senderId, String senderName, String content) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundException("채팅방 없음"));

        ChatMessage message = ChatMessage.builder()
                .roomId(room.getId())
                .senderId(senderId)
                .memberName(senderName)
                .content(content)
                .build();

        return messageRepository.save(message);
    }

    public List<ChatMessageDto> getMessages(UUID roomId) {
        List<ChatMessageDto> list = messageRepository.findByRoomIdOrderBySentAtAsc(roomId).stream()
                .map(mesage -> ChatMessageDto.from(mesage))
                .toList();
        return list;
    }
}

