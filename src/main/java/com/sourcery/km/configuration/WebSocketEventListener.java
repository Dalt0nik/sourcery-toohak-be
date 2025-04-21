package com.sourcery.km.configuration;

import com.sourcery.km.configuration.util.SessionAttributeUtil;
import com.sourcery.km.dto.quizPlayer.QuizPlayerDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.time.Instant;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketEventListener {
    private final SimpMessagingTemplate messagingTemplate;

    @EventListener
    public void handleSessionSubscribeEvent(SessionSubscribeEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String destination = headerAccessor.getDestination();
        String connectionType = SessionAttributeUtil.safelyGetSessionAttribute(headerAccessor, "connectionType");

        if (destination == null) {
            return;
        }

        // Extract quiz session ID from destination
        String[] parts = destination.split("/");
        if (parts.length < 4) {
            return;
        }
        String quizSessionId = parts[3];

        SessionAttributeUtil.safelySetSessionAttribute(headerAccessor, "quizSessionId", quizSessionId);

        if ("player".equals(connectionType) && destination.matches("/topic/session/[^/]+/players")) {
            handlePlayerSubscription(headerAccessor, quizSessionId);
        }
        else if ("host".equals(connectionType) && destination.matches("/topic/session/[^/]+/host")) {
            handleHostSubscription(headerAccessor, quizSessionId);
        }
    }

    private void handlePlayerSubscription(StompHeaderAccessor headerAccessor, String quizSessionId) {
        QuizPlayerDTO player = SessionAttributeUtil.safelyGetSessionAttribute(headerAccessor, "player");

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

            log.info("Player {} joined session {}", player.getNickname(), quizSessionId);
        }
    }

    private void handleHostSubscription(StompHeaderAccessor headerAccessor, String quizSessionId) {
        String userId = SessionAttributeUtil.safelyGetSessionAttribute(headerAccessor, "userId");

        if (userId != null && headerAccessor.getSessionAttributes() != null) {
            SessionAttributeUtil.safelySetSessionAttribute(headerAccessor, "isHost", true);
            log.info("Host {} joined session {}", userId, quizSessionId);
        }
    }

    @EventListener
    public void handleSessionDisconnectEvent(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String quizSessionId = SessionAttributeUtil.safelyGetSessionAttribute(headerAccessor, "quizSessionId");

        if (quizSessionId == null) {
            return;
        }

        // Handle host disconnection
        Boolean isHost = SessionAttributeUtil.safelyGetSessionAttribute(headerAccessor, "isHost");
        if (Boolean.TRUE.equals(isHost)) {
            String userId = SessionAttributeUtil.safelyGetSessionAttribute(headerAccessor, "userId");
            if (userId != null) {
                messagingTemplate.convertAndSend(
                        "/topic/session/" + quizSessionId + "/all",
                        Map.of(
                                "event", "host_disconnected",
                                "timestamp", Instant.now().toString()
                        )
                );

                log.info("Host {} disconnected from session {}", userId, quizSessionId);
            }
        }
        // Handle player disconnection
        else {
            QuizPlayerDTO player = SessionAttributeUtil.safelyGetSessionAttribute(headerAccessor, "player");
            if (player != null) {
                messagingTemplate.convertAndSend(
                        "/topic/session/" + quizSessionId + "/host",
                        Map.of(
                                "event", "player_disconnected",
                                "player", player,
                                "timestamp", Instant.now().toString()
                        )
                );

                log.info("Player {} disconnected from session {}", player.getNickname(), quizSessionId);
            }
        }
    }
}
