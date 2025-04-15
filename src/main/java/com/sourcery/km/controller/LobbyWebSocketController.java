package com.sourcery.km.controller;

import com.sourcery.km.dto.AnswerDTO;
import com.sourcery.km.service.QuizSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class LobbyWebSocketController {

    private final QuizSessionService lobbyService;

    @MessageMapping("/answer")
    public void submitAnswer(AnswerDTO answer) {
        lobbyService.processPlayerAnswer(answer);
    }
}

