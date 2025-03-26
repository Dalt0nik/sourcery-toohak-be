package com.sourcery.km.mapper;

import com.sourcery.km.entity.Quiz;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface QuizMapper {
    @Insert("INSERT INTO quizzes(quiz_id, created_by, title, description)" +
            " VALUES(#{id}, #{createdBy}, #{title}, #{description})")
    //@Options(useGeneratedKeys = true, keyProperty = "id")
    void insertQuiz(Quiz quiz);
}
