package com.sourcery.km.service;

import com.sourcery.km.DTO.AnswerDTO;
import com.sourcery.km.DTO.NewQuestionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

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
        System.out.println("Lobby:" + answer.getLobbyId() + ", player: " + answer.getPlayerId() +
                ", answer: " + answer.getAnswer());
    }
}
