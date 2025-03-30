package com.sourcery.km.repository;

import com.sourcery.km.entity.Quiz;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Mapper
@Repository
public interface QuizRepository {
    @Insert("INSERT INTO quizzes(id, created_by, title, description)" +
            " VALUES(#{id}, #{created_by}, #{title}, #{description})")
    void insertQuiz(Quiz quiz);

    @Select("""
    SELECT q.id, q.created_by, q.title, q.description, q.created_at, q.updated_at
    FROM quizzes q
    JOIN app_users u ON q.created_by = u.id
    WHERE u.auth0_id = #{sub}
    ORDER BY q.created_at DESC
""")
    List<Quiz> getQuizzesByAuth0Id(String sub);
}
