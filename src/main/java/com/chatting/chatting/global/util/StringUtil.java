package com.chatting.chatting.global.util;

public class StringUtil {
    public static String extractTokenFromAuthHeader(String header) {
        if (header == null || !header.startsWith("Bearer ")) throw new RuntimeException("토큰 없음");
        return header.substring(7);
    }
}
