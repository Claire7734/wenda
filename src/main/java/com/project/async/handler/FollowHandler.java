package com.project.async.handler;

import com.project.async.EventHandler;
import com.project.async.EventModel;
import com.project.async.EventType;
import com.project.model.EntityType;
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
 * Created by Claire on 2/1/18.
 */
@Component
public class FollowHandler implements EventHandler {

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
        if (model.getEntityType() == EntityType.ENTITY_QUESTION) {
            message.setContent("用户" + user.getName()
                    + "关注了你的问题, localhost/question/" + model.getEntityId());
        } else if (model.getEntityType() == EntityType.ENTITY_USER) {
            message.setContent("用户" + user.getName()
                    + "关注了你, localhost/user/" + model.getActorId());
        }
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.FOLLOW);
    }
}
