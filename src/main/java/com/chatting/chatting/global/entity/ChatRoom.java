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
@Table(name = "chat_room")
public class ChatRoom {

    @Id
    private UUID id;

    @Column("name")
    private String name;

    @Column("created_at")
    private OffsetDateTime createdAt;

    public static ChatRoom create(String name) {
        return ChatRoom.builder()
                .id(UuidCreator.getTimeOrdered()) // UUIDv7 스타일
                .name(name)
                .createdAt(OffsetDateTime.now())
                .build();
    }
}
