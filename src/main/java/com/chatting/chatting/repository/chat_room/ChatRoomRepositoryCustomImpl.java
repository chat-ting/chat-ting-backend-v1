package com.chatting.chatting.repository.chat_room;

import com.chatting.chatting.global.entity.ChatRoom;
import com.chatting.chatting.global.entity.QChatRoom;
import com.chatting.chatting.global.entity.QChatRoomMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RequiredArgsConstructor
public class ChatRoomRepositoryCustomImpl implements ChatRoomRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    // Implement the custom methods defined in ChatRoomRepositoryCustom interface here
    private static final QChatRoom chatRoom = QChatRoom.chatRoom;
    private static final QChatRoomMember chatRoomMember = QChatRoomMember.chatRoomMember;

    @Override
    public List<ChatRoom> findChatRoomsByMemberId(long memberId) {
        // Custom implementation to find chat rooms by member ID
        return queryFactory.select(chatRoom)
                .from(chatRoom)
                .innerJoin(chatRoomMember)
                    .on(chatRoomMember.room.eq(chatRoom))
                .where(chatRoomMember.userId.eq(memberId))
                .fetch();
    }
}
