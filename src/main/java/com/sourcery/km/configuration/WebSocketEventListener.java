package com.sourcery.km.configuration;

import com.sourcery.km.dto.quizPlayer.QuizPlayerDTO;
import com.sourcery.km.service.JwtService;
import io.jsonwebtoken.Claims;
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

        if (destination != null && destination.matches("/topic/quiz/[^/]+/players")) {

            String[] parts = destination.split("/");
            String quizSessionId = parts[3]; // Extract session ID from destination

            // Get JWT from session attributes (stored during connect)
            String jwt = (String) headerAccessor.getSessionAttributes().get("jwt");

            if (jwt != null) {
                try {
                    QuizPlayerDTO player = jwtService.getPlayerFromToken(jwt);

                    // Store player info for disconnect handling
                    headerAccessor.getSessionAttributes().put("player", player);
                    headerAccessor.getSessionAttributes().put("quizSessionId", quizSessionId);

                    // Notify host about new player
                    messagingTemplate.convertAndSend(
                            "/topic/quiz/" + quizSessionId + "/host",
                            Map.of(
                                    "event", "player_joined",
                                    "player", player,
                                    "timestamp", Instant.now().toString()
                            )
                    );

                    logger.info("Player {} joined session {}", player.getNickname(), quizSessionId);
                } catch (Exception e) {
                    //TODO: handle exception
                    logger.error("Error processing subscription: {}", e.getMessage());
                }
            }
        }
    }

    @EventListener
    public void handleSessionDisconnectEvent(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        // Retrieve stored player info
        QuizPlayerDTO player = (QuizPlayerDTO) headerAccessor.getSessionAttributes().get("player");
        String quizSessionId = (String) headerAccessor.getSessionAttributes().get("quizSessionId");

        if (player != null && quizSessionId != null) {
            messagingTemplate.convertAndSend(
                    "/topic/quiz/" + quizSessionId + "/host",
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
