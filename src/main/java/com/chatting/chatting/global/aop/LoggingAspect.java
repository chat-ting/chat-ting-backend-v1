package com.chatting.chatting.global.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    // HTTP 요청용
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void restControllerMethods() {}

    // WebSocket 메시지용
    @Pointcut("@annotation(org.springframework.messaging.handler.annotation.MessageMapping)")
    public void messageMappingMethods() {}

    @Around("restControllerMethods()")
    public Object logHttp(ProceedingJoinPoint joinPoint) throws Throwable {
        // 위에서 준 logAround 코드 그대로 복붙
        long start = System.currentTimeMillis();

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();
        Object[] args = joinPoint.getArgs();

        log.info("➡️ {}.{} called with args: {}", className, methodName, Arrays.toString(args));

        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable t) {
            log.error("❌ Exception in {}.{}: {}", className, methodName, t.getMessage());
            throw t;
        }

        long duration = System.currentTimeMillis() - start;

        if (result instanceof ResponseEntity<?> response) {
            log.info("✅ {}.{} returned: {} ({}ms)", className, methodName, response.getStatusCode(), duration);
        } else {
            log.info("✅ {}.{} returned: {} ({}ms)", className, methodName, result, duration);
        }

        return result;
    }

    @Around("messageMappingMethods()")
    public Object logWs(ProceedingJoinPoint joinPoint) throws Throwable {
        // 위의 logWebSocketMessage 코드
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();
        Object[] args = joinPoint.getArgs();

        log.info("💬 WS → {}.{} called with payload: {}", className, methodName, Arrays.toString(args));

        Object result = joinPoint.proceed();

        log.info("💬 WS ← {}.{} returned: {}", className, methodName, result);

        return result;
    }
}
