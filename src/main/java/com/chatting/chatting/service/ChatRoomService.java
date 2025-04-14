package com.chatting.chatting.service;

import com.chatting.chatting.exception.NotFoundException;
import com.chatting.chatting.global.entity.ChatRoom;
import com.chatting.chatting.global.entity.ChatRoomMember;
import com.chatting.chatting.repository.ChatRoomMemberRepository;
import com.chatting.chatting.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository memberRepository;

    public ChatRoom createRoom(String name, Long creatorId) {
        ChatRoom room = ChatRoom.builder()
                .name(name)
                .build();
        chatRoomRepository.save(room);

        ChatRoomMember creator = ChatRoomMember.builder()
                .room(room)
                .userId(creatorId)
                .build();
        memberRepository.save(creator);

        return room;
    }

    public void inviteUser(UUID roomId, Long userId) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundException("채팅방 없음"));

        if (memberRepository.existsByRoomIdAndUserId(roomId, userId)) {
            throw new IllegalStateException("이미 초대됨");
        }

        ChatRoomMember member = ChatRoomMember.builder()
                .room(room)
                .userId(userId)
                .build();
        memberRepository.save(member);
    }
}