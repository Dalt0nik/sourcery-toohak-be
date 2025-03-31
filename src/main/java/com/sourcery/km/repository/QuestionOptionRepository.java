package com.sourcery.km.repository;

import com.sourcery.km.entity.QuestionOption;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface QuestionOptionRepository {

    @Insert({
        "<script>",
        "INSERT INTO question_options (id, question_id, title, ordering, is_correct) VALUES ",
        "<foreach collection='questionsOptions' item='questionOption' separator=','>",
        "(#{questionOption.id}, #{questionOption.questionId}, #{questionOption.title},",
        " #{questionOption.ordering}, #{questionOption.isCorrect})",
        "</foreach>",
        "</script>"
    })
    void insertQuestionOptions(@Param("questionsOptions") List<QuestionOption> questionsOptions);
}
