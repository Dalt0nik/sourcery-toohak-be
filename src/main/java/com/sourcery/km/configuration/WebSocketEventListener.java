package com.sourcery.km.configuration;

import com.sourcery.km.dto.quizPlayer.QuizPlayerDTO;
import com.sourcery.km.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.time.Instant;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {
    private final SimpMessagingTemplate messagingTemplate;
    private final JwtService jwtService;
    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    @EventListener
    public void handleSessionSubscribeEvent(SessionSubscribeEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String destination = headerAccessor.getDestination();
        String connectionType = (String) headerAccessor.getSessionAttributes().get("connectionType");

        if (destination == null) {
            return;
        }

        logger.info("destination: {}", destination);
        // Extract quiz session ID from destination
        String[] parts = destination.split("/");
        if (parts.length < 4) {
            return;
        }
        String quizSessionId = parts[3];

        headerAccessor.getSessionAttributes().put("quizSessionId", quizSessionId);

        if ("player".equals(connectionType) && destination.matches("/topic/session/[^/]+/players")) {
            handlePlayerSubscription(headerAccessor, quizSessionId);
        }
        else if ("host".equals(connectionType) && destination.matches("/topic/session/[^/]+/host")) {
            handleHostSubscription(headerAccessor, quizSessionId);
        }
    }

    private void handlePlayerSubscription(StompHeaderAccessor headerAccessor, String quizSessionId) {
        QuizPlayerDTO player = (QuizPlayerDTO) headerAccessor.getSessionAttributes().get("player");

        if (player != null) {
            // Notify host that player has joined
            messagingTemplate.convertAndSend(
                    "/topic/session/" + quizSessionId + "/host",
                    Map.of(
                            "event", "player_joined",
                            "player", player,
                            "timestamp", Instant.now().toString()
                    )
            );

            logger.info("Player {} joined session {}", player.getNickname(), quizSessionId);
        }
    }

    private void handleHostSubscription(StompHeaderAccessor headerAccessor, String quizSessionId) {
        String userId = (String) headerAccessor.getSessionAttributes().get("userId");

        if (userId != null) {
            headerAccessor.getSessionAttributes().put("isHost", true);

            logger.info("Host {} connected to session {}", userId, quizSessionId);
        }
    }

    @EventListener
    public void handleSessionDisconnectEvent(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String quizSessionId = (String) headerAccessor.getSessionAttributes().get("quizSessionId");

        if (quizSessionId == null) {
            return;
        }

        // Handle host disconnection
        if (Boolean.TRUE.equals(headerAccessor.getSessionAttributes().get("isHost"))) {
            String userId = (String) headerAccessor.getSessionAttributes().get("userId");
            if (userId != null) {
                messagingTemplate.convertAndSend(
                        "/topic/session/" + quizSessionId + "/all",
                        Map.of(
                                "event", "host_disconnected",
                                "userId", userId,
                                "timestamp", Instant.now().toString()
                        )
                );

                logger.info("Host {} disconnected from session {}", userId, quizSessionId);
            }
        }
        // Handle player disconnection
        else {
            QuizPlayerDTO player = (QuizPlayerDTO) headerAccessor.getSessionAttributes().get("player");
            if (player != null) {
                messagingTemplate.convertAndSend(
                        "/topic/session/" + quizSessionId + "/host",
                        Map.of(
                                "event", "player_disconnected",
                                "player", player,
                                "timestamp", Instant.now().toString()
                        )
                );

                logger.info("Player {} disconnected from session {}", player.getNickname(), quizSessionId);
            }
        }
    }
}