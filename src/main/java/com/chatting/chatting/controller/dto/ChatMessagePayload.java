package com.chatting.chatting.controller.dto;

import java.util.UUID;

public record ChatMessagePayload(
        UUID roomId,
        String content
) {}
