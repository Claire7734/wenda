package com.project.service;

import com.project.dao.MessageDao;
import com.project.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Claire on 12/11/17.
 */
@Service
public class MessageService {

    @Autowired
    MessageDao messageDao;

    @Autowired
    SensitiveWordService sensitiveWordService;

    public int addMessage(Message message) {
        message.setContent(sensitiveWordService.filter(message.getContent()));
        return messageDao.addMessage(message) > 0 ? message.getMessageId() : 0;//todo 为什么message的id被更新了？？？
    }

    public List<Message> getConversationDetail(String conversationId, int offset, int limit) {
        return messageDao.getConversationDetail(conversationId, offset, limit);
    }
}
