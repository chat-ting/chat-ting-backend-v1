package com.chatting.chatting.service;

import com.chatting.chatting.controller.dto.ChatRoomDto;
import com.chatting.chatting.exception.NotFoundException;
import com.chatting.chatting.global.entity.ChatRoom;
import com.chatting.chatting.global.entity.ChatRoomMember;
import com.chatting.chatting.repository.ChatRoomMemberRepository;
import com.chatting.chatting.repository.chat_room.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
// @Transactional(readOnly = true)
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final TransactionalOperator tx;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    // @Transactional
    public Mono<ChatRoom> createRoom(String name, Long creatorId) {
        ChatRoom room = ChatRoom.create(name);

        Mono<ChatRoom> logic = chatRoomRepository.save(room)
                .flatMap(savedRoom -> {
                    ChatRoomMember creator = ChatRoomMember.create(savedRoom.getId(),creatorId);
                    return r2dbcEntityTemplate.insert(ChatRoomMember.class).using(creator)
                            .thenReturn(savedRoom);
                });
        return tx.transactional(logic);
    }

    public Mono<Void> inviteUser(UUID roomId, Long userId){
        return chatRoomRepository.findById(roomId)
                .switchIfEmpty(Mono.error(new NotFoundException("Chat room not found"))) // findById의 Mono가 비어있는경우 동작, 동작이 에러일경우 에러 던지고 체인 종료
                .flatMap(room -> //찾은 Mono의 내부 값을 풀어서 room 변수에 담고 동작
                    chatRoomMemberRepository.existsByRoomIdAndUserId(roomId, userId)
                            .flatMap(exists -> {//찾은 exists의 Mono를 풀어서 동작
                                if(exists){
                                    return Mono.error(new IllegalStateException("User already in room"));
                                }
                                ChatRoomMember member = ChatRoomMember.create(roomId, userId);
                                return r2dbcEntityTemplate.insert(ChatRoomMember.class).using(member).then();//return은 왜하지?
                            })
                );
    }




    public Flux<ChatRoomDto> getChatRoomsByMemberId(Long memberId) {
        return chatRoomRepository.findChatRoomsByMemberId(memberId)
                .map(ChatRoomDto::from);
    }
}