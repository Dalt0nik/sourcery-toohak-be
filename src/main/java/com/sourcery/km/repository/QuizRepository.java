package com.sourcery.km.repository;

import com.sourcery.km.dto.quiz.QuizCardDTO;
import com.sourcery.km.entity.Quiz;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Mapper
@Repository
public interface QuizRepository {
    @Insert("INSERT INTO quizzes(id, created_by, title, description)" +
            " VALUES(#{id}, #{createdBy}, #{title}, #{description})")
    void insertQuiz(Quiz quiz);

    @Select("SELECT * FROM quizzes WHERE id = #{id}")
    Optional<Quiz> findById(@Param("id") UUID id);

    @Select("""
    SELECT 
        q.id, 
        q.created_by, 
        q.title, 
        q.description, 
        q.created_at, 
        q.updated_at, 
        COUNT(ques.id) AS question_amount
    FROM quizzes q
    LEFT JOIN questions ques ON ques.quiz_id = q.id
    WHERE q.created_by = #{userId}
    GROUP BY 
        q.id, q.created_by, q.title, q.description, q.created_at, q.updated_at
    ORDER BY q.created_at DESC
        """)
    List<QuizCardDTO> getQuizCardsByUserId(@Param("userId") UUID userId);
}
