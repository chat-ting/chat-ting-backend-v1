package com.chatting.chatting.global.entity;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ChatMessage {

    @Id
    private UUID id;

    @Column(name = "room_id", nullable = false)
    private UUID roomId;

    @Column(name = "sender_id", nullable = false)
    private Long senderId;

    @Column(name = "member_name", nullable = false)
    private String memberName;

    @Column(nullable = false)
    private String content;

    @Column(name = "sent_at", nullable = false)
    private OffsetDateTime sentAt;

    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.id = UuidCreator.getTimeOrdered(); // UUIDv7 스타일
        }
        if (this.sentAt == null) {
            this.sentAt = OffsetDateTime.now();
        }
    }
}
