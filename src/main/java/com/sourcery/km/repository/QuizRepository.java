package com.sourcery.km.repository;

import com.sourcery.km.dto.quiz.QuizCardDTO;
import com.sourcery.km.entity.Question;
import com.sourcery.km.entity.Quiz;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Mapper
@Repository
public interface QuizRepository {
    @Insert("INSERT INTO quizzes(id, created_by, title, description, cover_image_id)" +
            " VALUES(#{id}, #{createdBy}, #{title}, #{description}, #{coverImageId})")
    void insertQuiz(Quiz quiz);

    @Select("SELECT * FROM quizzes WHERE id = #{id}")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "questions", javaType = List.class,
                    column = "id", many = @Many(select = "getQuestionsByQuizId"))})
    Optional<Quiz> findById(@Param("id") UUID id);

    //Shown like "no usages", but it's used in findById method.
    @Select("SELECT * FROM questions WHERE quiz_id = #{id}")
    List<Question> getQuestionsByQuizId(UUID id);

    @Update("UPDATE quizzes SET title = #{title}, description = #{description}, updated_at = NOW() " +
            "WHERE id = #{id}")
    void update(Quiz quiz);

    @Select("""
            SELECT 
                q.id, 
                q.created_by, 
                q.title, 
                q.description, 
                q.cover_image_id,
                q.created_at, 
                q.updated_at, 
                COUNT(ques.id) AS question_amount
            FROM quizzes q
            LEFT JOIN questions ques ON ques.quiz_id = q.id
            WHERE q.created_by = #{userId}
            GROUP BY 
                q.id, q.created_by, q.title, q.description, q.cover_image_id, q.created_at, q.updated_at
            ORDER BY q.created_at DESC
            """)
    List<QuizCardDTO> getQuizCardsByUserId(@Param("userId") UUID userId);

    @Delete("DELETE from quizzes where id = #{id}")
    void deleteQuiz(@Param("id") UUID id);
}
