package com.sourcery.km.configuration;

import com.sourcery.km.dto.quizPlayer.QuizPlayerDTO;
import com.sourcery.km.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.Collections;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
@Slf4j
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final JwtService jwtService;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Topic structure:
        // /topic/session/{quizSessionId}/{hostPass}/host - Messages intended for quiz hosts
        // /topic/session/{quizSessionId}/players - Messages intended for quiz players
        // /topic/session/{quizSessionId}/all - Messages intended for all participants
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

                // Intercept CONNECT message, which is the handshake
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String authHeader = accessor.getFirstNativeHeader("Authorization");
                    if (authHeader != null && authHeader.startsWith("Bearer ")) {
                        String jwt = authHeader.substring(7);

                        try {
                            boolean isPlayerToken = jwtService.isPlayerToken(jwt);

                            if (isPlayerToken) {
                                QuizPlayerDTO player = jwtService.getPlayerFromToken(jwt);
                                accessor.getSessionAttributes().put("connectionType", "player");
                                accessor.getSessionAttributes().put("player", player);
                            } else {
                                if (jwtService.validateToken(jwt)) {
                                    String userId = jwtService.extractId(jwt);
                                    accessor.getSessionAttributes().put("connectionType", "host");
                                    accessor.getSessionAttributes().put("userId", userId);
                                }
                            }

                            // Store token for later use
                            accessor.getSessionAttributes().put("jwt", jwt);
                        } catch (Exception e) {
                            log.warn("WebSocket auth failed", e);
                        }
                    }
                }
                return message;
            }
        });
    }
}
