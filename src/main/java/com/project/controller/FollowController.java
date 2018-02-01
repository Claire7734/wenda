package com.project.controller;

import com.project.async.EventModel;
import com.project.async.EventProducer;
import com.project.async.EventType;
import com.project.dto.HostHolder;
import com.project.dto.ViewObject;
import com.project.model.EntityType;
import com.project.model.Question;
import com.project.model.User;
import com.project.service.CommentService;
import com.project.service.FollowService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Claire on 2/1/18.
 */
@Controller
public class FollowController {
    private static final Logger logger = LoggerFactory.getLogger(FollowController.class);

    @Autowired
    FollowService followService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    EventProducer eventProducer;

    @Autowired
    QuestionService questionService;

    @Autowired
    CommentService commentService;

    @RequestMapping(path = "followUser", method = RequestMethod.POST)
    @ResponseBody
    public String follow(@RequestParam("userId") int userId) {
        if (hostHolder.getUser() == null) {
            return WendaUtil.getJSONString(999);
        }

        boolean ret = followService.follow(hostHolder.getUser().getUserId(), EntityType.ENTITY_USER, userId);
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(hostHolder.getUser().getUserId()).setEntityId(userId)
                .setEntityType(EntityType.ENTITY_USER).setEntityOwnerId(userId));
        return WendaUtil.getJSONString(ret ? 0 :
                1, String.valueOf(followService.getFolloweeCount(hostHolder.getUser().getUserId(), EntityType.ENTITY_USER)));
    }


    @RequestMapping(path = "unfollowUser", method = RequestMethod.POST)
    @ResponseBody
    public String unfollow(@RequestParam("userId") int userId) {
        if (hostHolder.getUser() == null) {
            return WendaUtil.getJSONString(999);
        }

        boolean ret = followService.unfollow(hostHolder.getUser().getUserId(), EntityType.ENTITY_USER, userId);
        eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW)
                .setActorId(hostHolder.getUser().getUserId()).setEntityId(userId)
                .setEntityType(EntityType.ENTITY_USER).setEntityOwnerId(userId));
        return WendaUtil.getJSONString(ret ? 0 :
                1, String.valueOf(followService.getFolloweeCount(hostHolder.getUser().getUserId(), EntityType.ENTITY_USER)));
    }


    @RequestMapping(path = "followQuestion", method = RequestMethod.POST)
    @ResponseBody
    public String followQuestioin(@RequestParam("questionId") int questionId) {
        if (hostHolder.getUser() == null) {
            return WendaUtil.getJSONString(999);
        }

        Question q = questionService.selectById(questionId);
        if (q == null) {
            return WendaUtil.getJSONString(1, "问题不存在");
        }


        boolean ret = followService.follow(hostHolder.getUser().getUserId(), EntityType.ENTITY_QUESTION, questionId);
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(hostHolder.getUser().getUserId()).setEntityId(questionId)
                .setEntityType(EntityType.ENTITY_QUESTION).setEntityOwnerId(q.getUserId()));

        Map<String, Object> info = new HashMap<>();
        info.put("headUrl", hostHolder.getUser().getHeadUrl());
        info.put("name", hostHolder.getUser().getName());
        info.put("id", hostHolder.getUser().getUserId());
        info.put("count", followService.getFolloweeCount(EntityType.ENTITY_QUESTION, questionId));
        return WendaUtil.getJSONString(ret ? 0 : 1, info);
    }


    @RequestMapping(path = "unfollowQuestion", method = RequestMethod.POST)
    @ResponseBody
    public String unfollowQuestion(@RequestParam("questionId") int questionId) {
        if (hostHolder.getUser() == null) {
            return WendaUtil.getJSONString(999);
        }

        Question q = questionService.selectById(questionId);
        if (q == null) {
            return WendaUtil.getJSONString(1, "问题不存在");
        }

        boolean ret = followService.unfollow(hostHolder.getUser().getUserId(), EntityType.ENTITY_QUESTION, questionId);
        eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW)
                .setActorId(hostHolder.getUser().getUserId()).setEntityId(questionId)
                .setEntityType(EntityType.ENTITY_QUESTION).setEntityOwnerId(q.getUserId()));

        Map<String, Object> info = new HashMap<>();
        info.put("id", hostHolder.getUser().getUserId());
        info.put("count", followService.getFolloweeCount(EntityType.ENTITY_QUESTION, questionId));
        return WendaUtil.getJSONString(ret ? 0 : 1, info);
    }

    @RequestMapping(path = "/user/{userId}/followers", method = RequestMethod.GET)
    public String followers(@PathVariable("userId") int userId, Model model) {
        List<Integer> followerIds = followService.getFollowers(userId, EntityType.ENTITY_USER, 0, 10);//todo 分页
        if (hostHolder.getUser() != null) {
            model.addAttribute("followers", getUsersInfo(hostHolder.getUser().getUserId(), followerIds));
        } else {
            model.addAttribute("followers", getUsersInfo(0, followerIds));
        }
        model.addAttribute("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, userId));
        model.addAttribute("curUser", userService.getUserbyId(userId));
        return "followers";
    }

    @RequestMapping(path = "/user/{userId}/followees", method = RequestMethod.GET)
    public String followees(@PathVariable("userId") int userId, Model model) {
        List<Integer> followeeIds = followService.getFollowees(userId, EntityType.ENTITY_USER, 0, 10);//todo 分页
        if (hostHolder.getUser() != null) {
            model.addAttribute("followees", getUsersInfo(hostHolder.getUser().getUserId(), followeeIds));
        } else {
            model.addAttribute("followees", getUsersInfo(0, followeeIds));
        }
        model.addAttribute("followeeCount", followService.getFollowerCount(userId, EntityType.ENTITY_USER));
        model.addAttribute("curUser", userService.getUserbyId(userId));
        return "followees";
    }

    private List<ViewObject> getUsersInfo(int localUserId, List<Integer> userIds) {
        List<ViewObject> userInfos = new ArrayList<>();
        for (Integer uid : userIds) {
            User user = userService.getUserbyId(uid);
            if (user == null)
                continue;

            ViewObject vo = new ViewObject();
            vo.set("user", user);
            vo.set("commentCount",commentService.getUserCommentCount(uid));
            vo.set("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, uid));
            vo.set("followeeCount", followService.getFolloweeCount(uid, EntityType.ENTITY_USER));
            if (localUserId != 0) { //是否登录
                vo.set("followed", followService.isFollower(localUserId, EntityType.ENTITY_USER, uid));
            } else {
                vo.set("followed", false);
            }
            userInfos.add(vo);
        }
        return userInfos;
    }
}
