package com.sourcery.km.mapper;

import com.sourcery.km.entity.Quiz;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface QuizMapper {
    @Insert("INSERT INTO quiz(title, description) VALUES(#{title}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertQuiz(Quiz quiz);
}
