package com.chatting.chatting.repository.chat_room;

import com.chatting.chatting.global.entity.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepositoryCustomImpl implements ChatRoomRepositoryCustom {
    private final DatabaseClient databaseClient;


    @Override
    public Flux<ChatRoom> findChatRoomsByMemberId(long memberId){
        String sql = """
                SELECT r.*
                FROM chat_room r
                INNER JOIN chat_room_member m ON r.id = m.room_id
                WHERE m.user_id = :memberId
                """;
        return databaseClient.sql(sql)
                .bind("memberId",memberId)
                .map((row,metadata) -> ChatRoom.builder()
                        .id(row.get("id", UUID.class))
                        .name(row.get("name", String.class))
                        .createdAt(row.get("created_at", OffsetDateTime.class))
                        .build())
                .all();
    }
}
