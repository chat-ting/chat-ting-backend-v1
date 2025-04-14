package com.chatting.chatting.repository;

import com.chatting.chatting.global.entity.ChatRoomMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.List;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, UUID> {

    List<ChatRoomMember> findByRoomId(UUID roomId);
    boolean existsByRoomIdAndUserId(UUID roomId, Long userId);
}