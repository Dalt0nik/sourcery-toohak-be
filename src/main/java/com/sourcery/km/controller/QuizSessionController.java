package com.sourcery.km.controller;

import com.sourcery.km.dto.NewQuestionDTO;
import com.sourcery.km.dto.quizPlayer.QuizPlayerDTO;
import com.sourcery.km.dto.quizSession.*;
import com.sourcery.km.service.JwtService;
import com.sourcery.km.service.QuizSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * The session goes as following:
 * 1. /create - Quiz owner creates a session.
 * 2. /find/{joinId} - People join using QR code or in website. Finds if the session is valid.
 * 3. /join - The person puts in the nickname and presses join. Receives a JWT.
 * 4. /start - Session owner starts the session and questions begin.
 * 5. /rejoin - if user is disconnected he can easily rejoin based on JWT
 */
@RestController
@RequestMapping("/sessions")
@RequiredArgsConstructor
public class QuizSessionController {

    private final QuizSessionService quizSessionService;
    private final JwtService jwtService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create")
    public QuizSessionDTO create(@RequestBody CreateSessionDTO quiz) {
        return quizSessionService.createNewSession(quiz);
    }

    @GetMapping("/find/{joinId}")
    public QuizSessionDTO find(@PathVariable String joinId) {
        return quizSessionService.getQuizSession(joinId);
    }

    /**
     * TODO: make handshake for websockets
     */
    @PostMapping("/join")
    public JoinSessionDTO registerAnonymousUser(@RequestBody JoinSessionRequestDTO request) {
        QuizPlayerDTO anonymousUser = quizSessionService.joinSession(request);
        return jwtService.createNewSession(anonymousUser);
    }

    /**
     * TODO: send updates to all connected clients of websocket
     */
    @PostMapping("/start")
    public void start(@RequestBody StartSessionDTO session) {
        quizSessionService.startSession(session);
    }

    /**
     * TODO: implement logic for sending answers and receive answers
     */
    @PostMapping("/send")
    public void sendQuestion(@RequestBody NewQuestionDTO question) {
        quizSessionService.sendNewQuestion(question);
    }

    /**
     * TODO: implement rejoin logic based on the spring jwt provided
     */
    @GetMapping("/rejoin")
    public void rejoin() {

    }
}
