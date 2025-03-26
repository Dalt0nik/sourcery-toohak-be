package com.sourcery.km.service;

import com.sourcery.km.dto.AnswerDTO;
import com.sourcery.km.dto.NewQuestionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LobbyService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendGameUpdate(String lobbyId) {
        messagingTemplate.convertAndSend("/topic/lobby/" + lobbyId, "Game has started!");
    }

    public void sendNewQuestion(String lobbyId, NewQuestionDTO newQuestionDTO) {
        messagingTemplate.convertAndSend("/topic/lobby/" + lobbyId, newQuestionDTO);
    }

    public void processPlayerAnswer(AnswerDTO answer)
    {
        log.info("Lobby: {}, player: {}, answer: {}",
                answer.getLobbyId(),
                answer.getPlayerId(),
                answer.getAnswer());
    }
}
