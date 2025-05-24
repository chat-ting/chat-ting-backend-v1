package com.chatting.chatting.global.entity;

import com.github.f4b6a3.uuid.UuidCreator;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table("chat_room_member")
public class ChatRoomMember {

    @Id
    private UUID id;

    @Column("room_id")
    private UUID roomId; // ChatRoom 자체가 아닌 roomId FK 값만 보관

    @Column("user_id")
    private Long userId;

    @Column("joined_at")
    private OffsetDateTime joinedAt;

    public static ChatRoomMember create(UUID roomId, Long userId) {
        return ChatRoomMember.builder()
                .id(UuidCreator.getTimeOrdered())
                .roomId(roomId)
                .userId(userId)
                .joinedAt(OffsetDateTime.now())
                .build();
    }
}
