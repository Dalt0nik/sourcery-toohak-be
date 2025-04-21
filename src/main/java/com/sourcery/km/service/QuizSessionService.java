package com.sourcery.km.service;

import com.sourcery.km.builder.quiz_player.QuizPlayerBuilder;
import com.sourcery.km.builder.quiz_session.QuizSessionBuilder;
import com.sourcery.km.dto.question.QuestionDTO;
import com.sourcery.km.dto.quizSession.AnswerDTO;
import com.sourcery.km.dto.quizPlayer.QuizPlayerDTO;
import com.sourcery.km.dto.quizSession.*;
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

import java.util.Map;
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
        messagingTemplate.convertAndSend("/topic/session/" + session.getQuizSessionId() + "/players", "Game has started!");
    }

    public QuizSessionDTO getQuizSession(String joinId) {
        QuizSessionWithOwner session = quizSessionRepository.findSessionByJoinId(joinId);
        if (session == null) {
            throw new BadRequestException("Quiz session not found");
        }
        return mapperService.map(session, QuizSessionDTO.class);
    }

    public QuizPlayerDTO joinSession(JoinSessionRequestDTO joinSessionRequestDTO) {
        QuizPlayer quizPlayer = QuizPlayerBuilder.createQuizPlayer(joinSessionRequestDTO);
        quizPlayerRepository.insertNewPlayer(quizPlayer);

        // Notify the host
        messagingTemplate.convertAndSend(
                "/topic/session/" + joinSessionRequestDTO.getQuizSessionId() + "/host",
                Map.of("event", "player_joined", "player", joinSessionRequestDTO.getNickname())
        );

        return mapperService.map(quizPlayer, QuizPlayerDTO.class);
    }

    public void sendQuestion(UUID sessionId, QuestionDTO questionDTO) {
        var user = jwtService.getAnonymousUserInfo();
        messagingTemplate.convertAndSend(
                "/topic/session/" + sessionId + "/players",
                questionDTO
        );
    }

    public void nextQuestion(UUID sessionId, UUID quizId) {
        // check whether user is the host
        // apply session question switch logic
        // call sendQuestion with the next question

        // For testing send first question of passed quiz
        QuestionDTO questionDTO = quizService.getQuizById(quizId).getQuestions().getFirst();

        // TODO: assign quiz to session entity and choose questions based on it
        // QuizSession session = quizSessionRepository.findSessionById(sessionId);


        // Currently just testing ws:
        messagingTemplate.convertAndSend(
                "/topic/session/" + sessionId + "/players",
                questionDTO
        );
    }

    public void processPlayerAnswer(AnswerDTO answer, UUID sessionId) {
        // This part is not implemented
        log.info("Session: {}, Player: {}, QuestionOption: {}",
                sessionId,
                answer.getPlayerId(),
                answer.getQuestionOptionId());
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
