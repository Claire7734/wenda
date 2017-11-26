package com.project.controller;

import com.project.aspect.LogAspect;
import com.project.dto.ViewObject;
import com.project.model.Question;
import com.project.service.QuestionService;
import com.project.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Claire on 11/26/17.
 */
@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Autowired
    UserService userService;
    @Autowired
    QuestionService questionService;

    @RequestMapping(path = {"/", "/index"}, method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("vos",getQuestions(1,0,10));
        return "index";
    }

    @RequestMapping(path = "/user/{userId}",method = RequestMethod.GET)
    public String userIndex(@PathVariable("userId") int userId,
                            Model model){
        model.addAttribute("vos",getQuestions(userId,0,10));
        return "index";
    }

    private List<ViewObject> getQuestions(int userid, int offset, int limit){
        List<Question> questionList = questionService.getLatestQuestions(userid, offset, limit);
        List<ViewObject> vos = new ArrayList<>();
        for (Question question:questionList){
            ViewObject vo = new ViewObject();
            vo.set("question",question);
            vo.set("user",userService.getUser(question.getUserId()));
            vos.add(vo);
        }
        return vos;
    }
}