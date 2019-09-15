package com.moqi.community01.mapper;


import com.moqi.community01.model.Question;



public interface QuestionExtMapper {
    int incView(Question record);
    int incCommentCount(Question record);
}