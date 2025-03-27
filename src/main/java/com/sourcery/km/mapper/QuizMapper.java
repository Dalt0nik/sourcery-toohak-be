package com.sourcery.km.mapper;

import com.sourcery.km.entity.Quiz;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface QuizMapper {
    @Insert("INSERT INTO quizzes(id, created_by, title, description)" +
            " VALUES(#{id}, #{createdBy}, #{title}, #{description})")
    void insertQuiz(Quiz quiz);
}
