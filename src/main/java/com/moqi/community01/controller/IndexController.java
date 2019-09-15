package com.moqi.community01.controller;

import com.moqi.community01.dto.PaginationDTO;
import com.moqi.community01.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;


@Controller
public class IndexController {
    @Autowired
    private QuestionService QuestionService;
    @GetMapping(path = "/")
    public String index(HttpServletRequest request, Model model,
                        @RequestParam(name = "page" , defaultValue = "1") Integer page,
                        @RequestParam(name = "size",defaultValue = "5")Integer size) {

        PaginationDTO pagination = QuestionService.list(page,size);
        model.addAttribute("pagination",pagination);
        return "index";
    }
}
