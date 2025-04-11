package com.sourcery.km.dto.quiz;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
public class QuizFlatRow {
    private UUID quizId;
    private String quizTitle;
    private String quizDescription;
    private UUID quizImageId;
    private UUID quizCreatedBy;
    //qqqqq
    private UUID questionId;
    private UUID questionImageId;
    private String questionTitle;
    //qq
    private UUID optionId;
    private String optionTitle;
    private Integer optionOrdering;
    private Boolean optionIsCorrect;
}

