package com.sourcery.km.repository;

import com.sourcery.km.entity.QuizPlayer;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface QuizPlayerRepository {

    @Insert("""
            INSERT INTO quiz_players (id, quiz_session_id, nickname, score, joined_at) VALUES
            (#{id}, #{quizSessionId}, #{nickname}, #{score}, #{joinedAt})
            """)
    void insertNewPlayer(QuizPlayer player);

    @Select("""
            SELECT * FROM quiz_players
            WHERE id = #{id}
            """)
    QuizPlayer getPlayerById(String id);
}
