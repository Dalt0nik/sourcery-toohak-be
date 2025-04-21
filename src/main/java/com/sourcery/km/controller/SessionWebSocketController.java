package com.sourcery.km.controller;

import com.sourcery.km.dto.quizSession.AnswerDTO;
import com.sourcery.km.service.QuizSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class SessionWebSocketController {

    private final QuizSessionService quizSessionService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/quiz/{sessionId}/answer")
    public void submitAnswer(@DestinationVariable String sessionId, AnswerDTO answer) {
        quizSessionService.processPlayerAnswer(answer, UUID.fromString(sessionId));

        // Notify the host about the answer
        messagingTemplate.convertAndSend(
                "/topic/quiz/" + sessionId + "/host",
                answer
        );
    }
}
