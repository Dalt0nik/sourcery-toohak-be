package com.sourcery.km.controller;

import com.sourcery.km.dto.NewQuestionDTO;
import com.sourcery.km.dto.quizSession.CreateSessionDTO;
import com.sourcery.km.service.LobbyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lobbies")
@RequiredArgsConstructor
public class LobbyController {

    private final LobbyService lobbyService;

    @PostMapping("/create")
    public String create(@RequestBody CreateSessionDTO quiz) {
        return lobbyService.createNewSession(quiz);
    }

    @PostMapping("/start/{lobbyId}")
    public void start(@PathVariable String lobbyId) {
        lobbyService.sendGameUpdate(lobbyId);
    }

    @GetMapping("/join/{lobbyId}")
    public String join(@PathVariable String lobbyId) {
        return "";
    }

    @PostMapping("/send/{lobbyId}")
    public void sendQuestion(@PathVariable String lobbyId, @RequestBody NewQuestionDTO question) {
        lobbyService.sendNewQuestion(lobbyId, question);
    }
}
