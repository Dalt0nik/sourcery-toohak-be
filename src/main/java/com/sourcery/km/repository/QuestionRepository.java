package com.sourcery.km.repository;

import com.sourcery.km.entity.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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
}
