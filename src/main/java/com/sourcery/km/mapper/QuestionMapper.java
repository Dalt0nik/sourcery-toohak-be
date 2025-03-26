package com.sourcery.km.mapper;

import com.sourcery.km.entity.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QuestionMapper {

    @Insert({
        "<script>",
        "INSERT INTO questions (question_id, quiz_id, image_id, title) VALUES ",
        "<foreach collection='questions' item='question' separator=','>",
        "(#{question.id}, #{question.quizId}, #{question.imageId}, #{question.title})",
        "</foreach>",
        "</script>"
    })
    void insertQuestions(@Param("questions") List<Question> questions);
}
