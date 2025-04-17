package com.sourcery.km.service;

import com.sourcery.km.builder.quiz_player.QuizPlayerBuilder;
import com.sourcery.km.builder.quiz_session.QuizSessionBuilder;
import com.sourcery.km.dto.AnswerDTO;
import com.sourcery.km.dto.NewQuestionDTO;
import com.sourcery.km.dto.quizPlayer.QuizPlayerDTO;
import com.sourcery.km.dto.quizSession.CreateSessionDTO;
import com.sourcery.km.dto.quizSession.JoinSessionRequestDTO;
import com.sourcery.km.dto.quizSession.QuizSessionDTO;
import com.sourcery.km.dto.quizSession.StartSessionDTO;
import com.sourcery.km.entity.Quiz;
import com.sourcery.km.entity.QuizPlayer;
import com.sourcery.km.entity.QuizSession;
import com.sourcery.km.exception.BadRequestException;
import com.sourcery.km.repository.QuizPlayerRepository;
import com.sourcery.km.repository.QuizSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuizSessionService {
    private final SimpMessagingTemplate messagingTemplate;

    private final QuizService quizService;

    private final QuizSessionRepository quizSessionRepository;

    private final MapperService mapperService;

    private final JwtService jwtService;

    private final QuizPlayerRepository quizPlayerRepository;

    public QuizSessionDTO createNewSession(CreateSessionDTO createSessionDTO) {
        UUID quizId = createSessionDTO.getQuizId();
        Quiz quiz = quizService.getQuiz(quizId);
        if (quiz == null) {
            throw new BadRequestException("Quiz not found");
        }
        QuizSession quizSession = QuizSessionBuilder.createQuizSession(quizId, createJoinId());
        quizSessionRepository.insertNewSession(quizSession);
        return mapperService.map(quizSession, QuizSessionDTO.class);
    }

    public void startSession(StartSessionDTO session) {
        // This part is not implemented fully
        messagingTemplate.convertAndSend("/topic/lobby/" + session.getQuizSessionId(), "Game has started!");
    }

    public QuizSessionDTO getQuizSession(String joinId) {
        QuizSession session = quizSessionRepository.findSessionByJoinId(joinId);
        if (session == null) {
            throw new BadRequestException("Quiz session not found");
        }
        return mapperService.map(session, QuizSessionDTO.class);
    }

    public QuizPlayerDTO joinSession(JoinSessionRequestDTO joinSessionRequestDTO) {
        QuizPlayer quizPlayer = QuizPlayerBuilder.createQuizPlayer(joinSessionRequestDTO);
        quizPlayerRepository.insertNewPlayer(quizPlayer);
        return mapperService.map(quizPlayer, QuizPlayerDTO.class);
    }

    public void sendNewQuestion(NewQuestionDTO newQuestionDTO) {
        var user = jwtService.getAnonymousUserInfo();
        // This part is not implemented fully
        messagingTemplate.convertAndSend("/topic/lobby/" + user.getQuizSessionId(), newQuestionDTO);
    }

    public void processPlayerAnswer(AnswerDTO answer) {
        // This part is not implemented
        log.info("Lobby: {}, player: {}, answer: {}",
                answer.getLobbyId(),
                answer.getPlayerId(),
                answer.getAnswer());
    }

    private String createJoinId() {
        final int JOIN_CODE_LENGTH = 5;
        String joinId;
        do {
            joinId = RandomStringUtils.insecure().nextAlphabetic(JOIN_CODE_LENGTH).toUpperCase();
        } while (quizSessionRepository.findSessionByJoinId(joinId) != null);
        return joinId;
    }
}
