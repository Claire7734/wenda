package com.project.async.handler;

import com.project.async.EventHandler;
import com.project.async.EventModel;
import com.project.async.EventType;
import com.project.model.Message;
import com.project.model.User;
import com.project.service.MessageService;
import com.project.service.UserService;
import com.project.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Claire on 12/14/17.
 */
@Component
public class LikeHandler implements EventHandler {

    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;

    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();
        message.setFromId(WendaUtil.SYSTEM_USERID);
        message.setToId(model.getEntityOwnerId());
        message.setCreatedDate(new Date());
        User user = userService.getUserbyId(model.getActorId());
        message.setContent("用户" + user.getName()
                + "赞了你的评论, localhost/question/" + model.getExt("questionId"));
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
