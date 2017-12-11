package com.project.controller;

import com.project.aspect.LogAspect;
import com.project.dto.HostHolder;
import com.project.dto.ViewObject;
import com.project.model.Comment;
import com.project.model.EntityType;
import com.project.model.Question;
import com.project.service.CommentService;
import com.project.service.QuestionService;
import com.project.service.UserService;
import com.project.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Claire on 12/4/17.
 */
@Controller
public class QuestionController {
    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    QuestionService questionService;
    @Autowired
    UserService userService;
    @Autowired
    CommentService commentService;

    @Autowired
    HostHolder hostHolder;

    @RequestMapping(value = "/question/add", method = RequestMethod.POST)
    @ResponseBody
    public String addQuestion(@RequestParam("title") String title,
                              @RequestParam("content") String content) {

        try {
            Question question = new Question(title, content);
            question.setCreatedDate(new Date());
            question.setCommentCount(0);
            if (hostHolder.getUser() == null) {
//                question.setUserId(WendaUtil.ANONYMOUS_USERID);
                return WendaUtil.getJSONString(999); //前端默认跳转到登陆页面
            } else {
                question.setUserId(hostHolder.getUser().getUserId());
            }
            if (questionService.addQuestion(question) > 0) {
                return WendaUtil.getJSONString(0);
            }
        } catch (Exception e) {
            logger.error("增加题目失败" + e.getMessage());
        }
        return WendaUtil.getJSONString(1, "失败");
    }

    @RequestMapping("/question/{questionId}")
    public String questionDetail(@PathVariable int questionId,
                                 Model model) {
        Question question = questionService.selectById(questionId);
        model.addAttribute("question", question);
        model.addAttribute("user", userService.getUserbyId(question.getUserId()));
        List<Comment> commentList = commentService.getCommentByEntity(questionId, EntityType.ENTITY_QUESTION);
        List<ViewObject> comments = new ArrayList<>();
        for (Comment comment : commentList) {
            ViewObject vo = new ViewObject();
            vo.set("comment", comment);
            vo.set("user", userService.getUserbyId(comment.getUserId()));
            comments.add(vo);
        }
        model.addAttribute("comments", comments);
        return "detail";
    }
}
