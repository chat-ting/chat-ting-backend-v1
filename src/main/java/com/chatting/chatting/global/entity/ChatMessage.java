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
@Table(name = "chat_message")
public class ChatMessage {

    @Id
    private UUID id;

    @Column("room_id")
    private UUID roomId;

    @Column("sender_id")
    private Long senderId;

    @Column("member_name")
    private String memberName;

    @Column("content")
    private String content;

    @Column("sent_at")
    private OffsetDateTime sentAt;


    public static ChatMessage create(UUID roomId, Long senderId, String memberName, String content) {
        return ChatMessage.builder()
                .id(UuidCreator.getTimeOrdered())
                .roomId(roomId)
                .senderId(senderId)
                .memberName(memberName)
                .content(content)
                .sentAt(OffsetDateTime.now())
                .build();
    }
}
