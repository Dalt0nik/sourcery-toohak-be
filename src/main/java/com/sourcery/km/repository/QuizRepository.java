package com.sourcery.km.repository;

import com.sourcery.km.entity.Quiz;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface QuizRepository {
    @Insert("INSERT INTO quizzes(id, created_by, title, description)" +
            " VALUES(#{id}, #{createdBy}, #{title}, #{description})")
    void insertQuiz(Quiz quiz);
}
