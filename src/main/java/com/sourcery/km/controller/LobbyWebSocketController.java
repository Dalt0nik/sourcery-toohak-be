package com.sourcery.km.controller;

import com.sourcery.km.DTO.AnswerDTO;
import com.sourcery.km.service.LobbyService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class LobbyWebSocketController {

    private final LobbyService lobbyService;

    public LobbyWebSocketController(LobbyService lobbyService) {
        this.lobbyService = lobbyService;
    }

    @MessageMapping("/answer")
    public void submitAnswer(AnswerDTO answer) {
        lobbyService.processPlayerAnswer(answer);
    }
}

