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

    /**
     * Sends a "Game has started!" message to all clients subscribed to the lobby's topic.
     *
     * @param lobbyId the ID of the lobby to which the message should be sent
     */
    public void sendGameUpdate(String lobbyId) {
        messagingTemplate.convertAndSend("/topic/lobby/" + lobbyId, "Game has started!");
    }

    /**
     * Sends a new question object to all clients in the specified lobby.
     *
     * @param lobbyId the lobby ID where the question should be broadcasted
     * @param newQuestionDTO the question payload containing lobbyId and question text
     */
    public void sendNewQuestion(String lobbyId, NewQuestionDTO newQuestionDTO) {
        messagingTemplate.convertAndSend("/topic/lobby/" + lobbyId, newQuestionDTO);
    }

    /**
     * Handles an answer submitted by a player. Currently just logs the submission to the console.
     *
     * @param answer the player's submitted answer, including lobby and player info
     */
    public void processPlayerAnswer(AnswerDTO answer)
    {
        System.out.println("Lobby:" + answer.getLobbyId() + ", player: " + answer.getPlayerId() +
                ", answer: " + answer.getAnswer());
    }
}
