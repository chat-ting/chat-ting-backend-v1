package com.chatting.chatting.controller.dto;

public record ChatKafkaMessage(
        ChatMessagePayload payload,
        AuthUserInfo user
) {}