package com.sourcery.km.controller;

import com.sourcery.km.DTO.NewQuestionDTO;
import com.sourcery.km.service.LobbyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lobby")
public class LobbyController {

    private final LobbyService lobbyService;

    public LobbyController(LobbyService lobbyService) {
        this.lobbyService = lobbyService;
    }

    @PostMapping("/{lobbyId}/start")
    public ResponseEntity<String> startGame(@PathVariable String lobbyId) {

        lobbyService.sendGameUpdate(lobbyId);

        return ResponseEntity.ok("Game started and players notified!");
    }

    @PostMapping("/{lobbyId}")
    public ResponseEntity<String> sendQuestion(@PathVariable String lobbyId, @RequestBody NewQuestionDTO question) {

        lobbyService.sendNewQuestion(lobbyId, question);

        return ResponseEntity.ok("New question has been sent!");
    }
}
