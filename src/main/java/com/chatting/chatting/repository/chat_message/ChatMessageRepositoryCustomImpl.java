package com.chatting.chatting.repository.chat_message;

import com.chatting.chatting.global.entity.ChatRoom;
import com.chatting.chatting.global.entity.QChatMessage;
import com.chatting.chatting.global.entity.QChatRoomMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
public class ChatMessageRepositoryCustomImpl implements ChatMessageRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private static final QChatMessage chatMessage = QChatMessage.chatMessage;


    // Implement custom methods here if needed
}
