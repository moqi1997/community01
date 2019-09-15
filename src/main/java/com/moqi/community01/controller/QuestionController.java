package com.moqi.community01.controller;

import com.moqi.community01.dto.CommentDTO;
import com.moqi.community01.dto.QuestionDTO;
import com.moqi.community01.enums.CommentTypeEnum;
import com.moqi.community01.service.CommentService;
import com.moqi.community01.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private CommentService commentService;
    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id")Integer id, Model model) {
       QuestionDTO questionDTO = questionService.getById(id);
        List<CommentDTO> commentDTOS = commentService.listByTargetId(id, CommentTypeEnum.QUESTION);
       //增加阅读数
       questionService.incView(id);
       model.addAttribute("question",questionDTO);
       model.addAttribute("comments",commentDTOS);
        return "question";
    }
}
