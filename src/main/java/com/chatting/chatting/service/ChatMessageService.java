package com.chatting.chatting.service;

import com.chatting.chatting.exception.NotFoundException;
import com.chatting.chatting.global.entity.ChatMessage;
import com.chatting.chatting.global.entity.ChatRoom;
import com.chatting.chatting.repository.ChatMessageRepository;
import com.chatting.chatting.repository.chat_room_repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository messageRepository;

    public ChatMessage saveMessage(UUID roomId, Long senderId, String content) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundException("채팅방 없음"));

        ChatMessage message = ChatMessage.builder()
                .room(room)
                .senderId(senderId)
                .content(content)
                .build();

        return messageRepository.save(message);
    }

    public List<ChatMessage> getMessages(UUID roomId) {
        return messageRepository.findByRoomIdOrderBySentAtAsc(roomId);
    }
}

