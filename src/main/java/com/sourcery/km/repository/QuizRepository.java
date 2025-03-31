package com.sourcery.km.repository;

import com.sourcery.km.entity.Quiz;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Mapper
@Repository
public interface QuizRepository {
    @Insert("INSERT INTO quizzes(id, created_by, title, description)" +
            " VALUES(#{id}, #{createdBy}, #{title}, #{description})")
    void insertQuiz(Quiz quiz);

    @Select("SELECT * FROM quizzes WHERE id = #{id}")
    Optional<Quiz> findById(@Param("id") UUID id);

    @Select("""
    SELECT q.id, q.created_by, q.title, q.description, q.created_at, q.updated_at
    FROM quizzes q
    WHERE q.created_by = #{user_id}
    ORDER BY q.created_at DESC
""")
    List<Quiz> getQuizzesByUserId(UUID user_id);
}
