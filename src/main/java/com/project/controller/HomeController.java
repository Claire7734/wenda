package com.project.controller;

import com.project.aspect.LogAspect;
import com.project.dto.HostHolder;
import com.project.dto.ViewObject;
import com.project.model.EntityType;
import com.project.model.Question;
import com.project.model.User;
import com.project.service.CommentService;
import com.project.service.FollowService;
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
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Claire on 11/26/17.
 */
@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    UserService userService;
    @Autowired
    QuestionService questionService;
    @Autowired
    FollowService followService;
    @Autowired
    CommentService commentService;

    @Autowired
    HostHolder hostHolder;

    @RequestMapping(path = {"/", "/index"}, method = RequestMethod.GET)
    public String index(Model model,
                        @RequestParam(value = "pop", defaultValue = "0") int pop) {
        model.addAttribute("vos", getQuestions(0, 0, 10));//QuesionDao中做过判定，如果userId为0，返回所有；
        return "index";
    }

    @RequestMapping(path = "/user/{userId}", method = {RequestMethod.GET, RequestMethod.POST})
    public String userProfile(@PathVariable("userId") int userId,
                              Model model) {
        model.addAttribute("vos", getQuestions(userId, 0, 10));

        User user = userService.getUserbyId(userId);
        ViewObject vo = new ViewObject();
        vo.set("user", user);
        vo.set("commentCount", commentService.getUserCommentCount(userId));
        vo.set("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, userId));
        vo.set("followeeCount", followService.getFolloweeCount(userId, EntityType.ENTITY_USER));
        if (hostHolder.getUser() != null) {
            vo.set("followed", followService.isFollower(hostHolder.getUser().getUserId(), EntityType.ENTITY_USER, userId));
        } else {
            vo.set("followed", false);
        }
        model.addAttribute("profileUser", vo);
        return "profile";
    }


    //工具
    private List<ViewObject> getQuestions(int userId, int offset, int limit) {
        List<Question> questionList = questionService.getLatestQuestions(userId, offset, limit);
        List<ViewObject> vos = new ArrayList<>();
        for (Question question : questionList) {
            ViewObject vo = new ViewObject();
            vo.set("question", question);
            vo.set("followCount", followService.getFollowerCount(EntityType.ENTITY_QUESTION, question.getQuestionId()));
            vo.set("user", userService.getUserbyId(question.getUserId()));
            vos.add(vo);
        }
        return vos;
    }
}
