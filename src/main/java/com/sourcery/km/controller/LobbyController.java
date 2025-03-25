package com.sourcery.km.controller;

import com.sourcery.km.dto.NewQuestionDTO;
import com.sourcery.km.service.LobbyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lobbies")
@RequiredArgsConstructor
public class LobbyController {

    private final LobbyService lobbyService;

    /**
     * Starts the game in the specified lobby and notifies all subscribed clients via WebSocket.
     *
     * @param lobbyId The ID of the lobby where the game is to be started.
     * @return A response confirming that the game has started.
     */
    @PostMapping("/{lobbyId}/start")
    public void startGame(@PathVariable String lobbyId) {

        lobbyService.sendGameUpdate(lobbyId);
    }

    /**
     * Sends a new question to all players in the specified lobby via WebSocket.
     *
     * @param lobbyId The ID of the lobby where the question should be sent.
     * @param question The question data to send, including lobbyId and question text.
     * @return A response confirming that the question has been sent.
     */
    @PostMapping("/{lobbyId}")
    public ResponseEntity<String> sendQuestion(@PathVariable String lobbyId, @RequestBody NewQuestionDTO question) {

        lobbyService.sendNewQuestion(lobbyId, question);

        return ResponseEntity.ok("New question has been sent!");
    }
}
