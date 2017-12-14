package com.project.controller;

import com.project.async.EventModel;
import com.project.async.EventProducer;
import com.project.async.EventType;
import com.project.dto.HostHolder;
import com.project.model.Comment;
import com.project.model.EntityType;
import com.project.model.User;
import com.project.service.CommentService;
import com.project.service.LikeService;
import com.project.service.UserService;
import com.project.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Claire on 12/13/17.
 */
@Controller
public class LikeController {
    private static final Logger logger = LoggerFactory.getLogger(LikeController.class);

    @Autowired
    LikeService likeService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    EventProducer eventProducer;
    @Autowired
    CommentService commentService;


    @Autowired
    UserService userService;

    @RequestMapping(path = "/like", method = RequestMethod.POST)
    @ResponseBody
    public String like(@RequestParam("commentId") int commentId) {
        User user = hostHolder.getUser();
        if (user == null) {
            user = userService.getUserbyId(10);
//            return WendaUtil.getJSONString(999);
        }

        Comment comment = commentService.getCommentById(commentId);
        eventProducer.fireEvent(new EventModel(EventType.LIKE).setActorId(user.getUserId())
                .setEntityId(EntityType.ENTITY_COMMENT).setEntityId(commentId)
                .setEntityOwnerId(comment.getUserId())
                .setExt("questionId", String.valueOf(comment.getEntityId())));

        long likeCount = likeService.like(user.getUserId(), EntityType.ENTITY_COMMENT, commentId);
        return WendaUtil.getJSONString(0, String.valueOf(likeCount));
    }

    @RequestMapping(path = "/dislike", method = RequestMethod.POST)
    @ResponseBody
    public String dislike(@RequestParam("commentId") int commentId) {
        User user = hostHolder.getUser();
        if (user == null) {
            return WendaUtil.getJSONString(999);
        }
        long likeCount = likeService.dislike(user.getUserId(), EntityType.ENTITY_COMMENT, commentId);
        return WendaUtil.getJSONString(0, String.valueOf(likeCount));
    }
}
