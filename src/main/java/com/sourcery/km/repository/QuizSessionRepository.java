package com.sourcery.km.repository;

import com.sourcery.km.dto.quizSession.QuizSessionWithOwner;
import com.sourcery.km.entity.QuizSession;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Mapper
@Repository
public interface QuizSessionRepository {

    @Insert("""
            INSERT INTO quiz_sessions (id, status, join_id, created_at, quiz_id) VALUES
            (#{id}, CAST(#{status} AS quiz_status), #{joinId}, #{createdAt}, #{quizId})
            """)
    void insertNewSession(QuizSession session);

    @Select("""
            SELECT qs.*, u.auth0_id FROM quiz_sessions qs
            JOIN quizzes ON qs.quiz_id = quizzes.id
            JOIN app_users u ON quizzes.created_by = u.id
            WHERE join_id = #{joinId}
            """)
    QuizSessionWithOwner findSessionByJoinId(String joinId);

    @Select("""
            SELECT * FROM quiz_sessions
            WHERE id = #{sessionId}
            """)
    QuizSession findSessionById(UUID sessionId);
}
