package com.sourcery.km.repository;

import com.sourcery.km.entity.Quiz;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Mapper
@Repository
public interface QuizRepository {
    @Insert("INSERT INTO quizzes(id, created_by, title, description)" +
            " VALUES(#{id}, #{createdBy}, #{title}, #{description})")
    void insertQuiz(Quiz quiz);

    @Select("SELECT * FROM quizzes WHERE id = #{id}")
    Optional<Quiz> findById(@Param("id") UUID id);

    @Update("UPDATE quizzes SET title = #{title}, description = #{description} " +
            "WHERE id = #{id}")
    void update(Quiz quiz);
}
