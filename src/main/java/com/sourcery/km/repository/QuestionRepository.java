package com.sourcery.km.repository;

import com.sourcery.km.entity.Question;
import com.sourcery.km.entity.QuestionOption;
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

    @Insert("""
    INSERT INTO questions (id, quiz_id, image, title)
    VALUES (#{id}, #{quizId}, #{imageId}, #{title})
        """)
    void insertQuestion(Question question);

    @Select("SELECT * FROM questions WHERE quiz_id = #{quizId}")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "questionOptions", javaType = List.class,
            column = "id", many = @Many(select = "getQuestionOptionsByQuestionId"))})
    List<Question> getQuestionsByQuizId (UUID quizId);

    //Shown like "no usages", but it's used in getQuestionsByQuizId method.
    @Select("SELECT * FROM question_options WHERE question_id = #{questionId}")
    List<QuestionOption> getQuestionOptionsByQuestionId (UUID questionId);

    @Delete("DELETE FROM questions WHERE quiz_id = #{quizId}")
    void deleteQuestionsByQuizId(UUID quizId);
}
