package com.sourcery.km.repository;

import com.sourcery.km.entity.Question;
import jakarta.validation.Valid;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Mapper
@Repository
public interface QuestionRepository {

    @Insert({
            "<script>",
            "INSERT INTO questions (id, quiz_id, image, title) VALUES ",
            "<foreach collection='questions' item='question' separator=','>",
            "(#{question.id}, #{question.quizId}, #{question.imageId}, #{question.title})",
            "</foreach>",
            "</script>"
    })
    void insertQuestions(@Param("questions") List<Question> questions);

    @Select("""
            SELECT q.id, q.quiz_id, q.title
            FROM questions q
            WHERE q.quiz_id = #{quizId}
            """)
    List<Question> getQuestionsByQuizId(@Param("quizId") UUID quizId);

    @Select("SELECT id FROM questions WHERE quiz_id = #{quizId}")
    List<UUID> findQuestionIdsByQuizId(UUID quizId);

    @Delete("DELETE FROM questions WHERE quiz_id = #{quizId}")
    void deleteQuestionsByQuizId(UUID quizId);

    @Update("""
                UPDATE questions
                SET title = #{question.title}
                WHERE id = #{question.id}
            """)
    void updateExistingQuestion(@Valid @Param("question") Question question);
}
