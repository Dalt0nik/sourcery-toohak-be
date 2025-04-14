package com.sourcery.km.repository;

import com.sourcery.km.entity.QuizSession;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface QuizSessionRepository {

    @Insert("""
            INSERT INTO quiz_sessions (id, status, join_id, created_at) VALUES
            (#{id}, CAST(#{status} AS quiz_status), #{joinId}, NOW())
            """)
    void insertNewSession(QuizSession session);

    @Select("""
            SELECT * FROM quiz_sessions
            WHERE id = #{id}
            """)
    QuizSession getQuizSessionById(String id);
}
