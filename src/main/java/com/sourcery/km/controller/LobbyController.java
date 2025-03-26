package com.sourcery.km.controller;

import com.sourcery.km.dto.NewQuestionDTO;
import com.sourcery.km.service.LobbyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lobbies")
@RequiredArgsConstructor
public class LobbyController {

    private final LobbyService lobbyService;

    @PostMapping("/{lobbyId}/start")
    public void startGame(@PathVariable String lobbyId) {

        lobbyService.sendGameUpdate(lobbyId);
    }

    @PostMapping("/{lobbyId}")
    public void sendQuestion(@PathVariable String lobbyId, @RequestBody NewQuestionDTO question) {

        lobbyService.sendNewQuestion(lobbyId, question);
    }
}
