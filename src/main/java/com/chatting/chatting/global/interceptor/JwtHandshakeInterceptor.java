package com.chatting.chatting.global.interceptor;

import com.chatting.chatting.controller.dto.AuthUserInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final RedisTemplate<String,String> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,//이건 모야? ServletRequest랑 다른건가? spring 내장 was랑 통신하는건가
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String,Object> attributes) throws Exception {
        log.info("WebSocket Handshake Interceptor: {}", request.getURI());

        //1. 토큰 추출
        // HttpHeaders headers = request.getHeaders();
        // String token = headers.getFirst("Authorization");

        String uri = request.getURI().toString(); //이게 나한테 들어온 요청 uri인가보지?
        String token = extractTokenFromUri(uri);//uri에 jwt 담으면 위험하지 않나 헤더가 낫지 않나

        String json = redisTemplate.opsForValue().get(token);
        if(json == null){
            log.info("❌ 토큰이 유효하지 않습니다.");
            return false;
        }

        AuthUserInfo user = objectMapper.readValue(json, AuthUserInfo.class);

        attributes.put("user", user);
        log.info("✅ WebSocket Handshake: {} / {}", user.getUsername(), token);
        return true;


    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        
    }

    private String extractTokenFromUri(String uri){
        if(!uri.contains("token=")){
            return null;
        }
        return uri.substring(uri.indexOf("token=")+6);
    }
}
