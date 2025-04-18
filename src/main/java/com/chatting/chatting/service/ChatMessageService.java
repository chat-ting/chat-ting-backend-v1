package com.chatting.chatting.service;

import com.chatting.chatting.controller.dto.ChatMessageDto;
import com.chatting.chatting.exception.NotFoundException;
import com.chatting.chatting.global.entity.ChatMessage;
import com.chatting.chatting.global.entity.ChatRoom;
import com.chatting.chatting.repository.ChatRoomMemberRepository;
import com.chatting.chatting.repository.chat_message.ChatMessageRepository;
import com.chatting.chatting.repository.chat_room.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository messageRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;

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

    public List<ChatMessageDto> getMessages(UUID roomId, Long senderId) {
        validateRoomAndSender(roomId, senderId);
        List<ChatMessageDto> list = messageRepository.findByRoomIdOrderByIdAsc(roomId).stream()
                .map(mesage -> ChatMessageDto.from(mesage))
                .toList();
        return list;
    }

    public ChatMessageDto getLastMessage(UUID roomId, Long senderId) {

        validateRoomAndSender(roomId, senderId);
        Optional<ChatMessage> lastMessage = messageRepository.findTopByRoomIdOrderByIdDesc(roomId);
        if(lastMessage.isEmpty()) {
            return ChatMessageDto.builder()
                    .id("-1")
                    .senderId(0L)
                    .content("빈 채팅방")
                    .sentAt(0L)
                    .senderName("SYSTEM")
                    .build();
        }
        return ChatMessageDto.from(lastMessage.get());
    }

    private void validateRoomAndSender(UUID roomId, Long senderId) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundException("채팅방 없음"));

        chatRoomMemberRepository.findByRoomIdAndUserId(room.getId(), senderId)
                .orElseThrow(() -> new NotFoundException("채팅방에 없음"));

    }


}

