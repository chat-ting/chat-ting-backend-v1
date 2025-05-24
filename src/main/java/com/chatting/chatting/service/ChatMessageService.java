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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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


    public Mono<ChatMessage> saveMessage(UUID roomId, Long senderId, String senderName, String content){
        return chatRoomRepository.findById(roomId)
                .switchIfEmpty(Mono.error(new NotFoundException("채팅방 없음")))
                .flatMap(room -> {
                    ChatMessage message = ChatMessage.create(room.getId(), senderId, senderName, content);
                    return messageRepository.save(message);
                });
    }


    public Flux<ChatMessageDto> getMessages(UUID roomId, Long senderId){
        return validateRoomAndSender(roomId, senderId)
                .thenMany(messageRepository.findByRoomIdOrderByIdAsc(roomId))
                .map(ChatMessageDto::from);
    }

    public Mono<ChatMessageDto> getLastMessage(UUID roomId, Long senderId) {
        return validateRoomAndSender(roomId, senderId)
                .then(messageRepository.findTopByRoomIdOrderByIdDesc(roomId)
                        .map(ChatMessageDto::from)
                        .defaultIfEmpty(ChatMessageDto.builder()
                                .id("-1")
                                .senderId(0L)
                                .content("빈 채팅방")
                                .sentAt(0L)
                                .senderName("SYSTEM")
                                .build()));
    }



    private Mono<Void> validateRoomAndSender(UUID roomId, Long senderId){
        return chatRoomRepository.findById(roomId)
                .switchIfEmpty(Mono.error(new NotFoundException("채팅방 없음")))
                .flatMap(room ->
                        chatRoomMemberRepository.findByRoomIdAndUserId(room.getId(), senderId)
                                .hasElements()
                                .flatMap(exists -> {
                                    if(!exists){
                                        return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN,"채팅방에 없음"));
                                    }
                                    return Mono.empty();
                                })
                );
    }


}

