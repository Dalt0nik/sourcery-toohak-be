package com.sourcery.km.controller;

import com.sourcery.km.dto.AnswerDTO;
import com.sourcery.km.service.LobbyService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class LobbyWebSocketController {

    private final LobbyService lobbyService;

    public LobbyWebSocketController(LobbyService lobbyService) {
        this.lobbyService = lobbyService;
    }

    /**
     * Receives an answer from a player via WebSocket and processes it.
     *
     * This method listens to the "/app/answer" destination. The answer data is expected
     * to include the lobby ID, player ID, and the selected answer.
     *
     * @param answer The player's answer including lobby and player info.
     */
    @MessageMapping("/answer")
    public void submitAnswer(AnswerDTO answer) {
        lobbyService.processPlayerAnswer(answer);
    }
}

