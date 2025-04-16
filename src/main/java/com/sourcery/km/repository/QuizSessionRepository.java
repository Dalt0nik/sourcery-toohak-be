package com.sourcery.km.repository;

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
            INSERT INTO quiz_sessions (id, status, join_id, created_at, created_by) VALUES
            (#{id}, CAST(#{status} AS quiz_status), #{joinId}, #{createdAt}, #{createdBy})
            """)
    void insertNewSession(QuizSession session);

    @Select("""
            SELECT * FROM quiz_sessions
            WHERE join_id = #{join_id}
            """)
    QuizSession findSessionByJoinId(String join_id);

    @Select("""
            SELECT u.id FROM quiz_sessions JOIN  u
            """)
    UUID findCreatorIdBySessionId(UUID sessionId);
}
