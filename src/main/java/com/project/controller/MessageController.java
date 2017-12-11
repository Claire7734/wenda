package com.project.controller;

import com.project.dto.HostHolder;
import com.project.dto.ViewObject;
import com.project.model.Message;
import com.project.model.User;
import com.project.service.MessageService;
import com.project.service.UserService;
import com.project.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Claire on 12/11/17.
 */
@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    MessageService messageService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    UserService userService;

    @RequestMapping(path = "/msg/list", method = RequestMethod.GET)
    public String getConversationList() {
        return "letter";
    }

    @RequestMapping(path = "/msg/detail", method = RequestMethod.GET)
    public String getConversationDetail(Model model,
                                        @RequestParam("conversationId") String conversationId) {

        try {
            List<Message> messageList = messageService.getConversationDetail(conversationId, 0, 10);
            List<ViewObject> messages = new ArrayList<>();
            for (Message message : messageList) {
                ViewObject vo = new ViewObject();
                vo.set("message", message);
                vo.set("user", userService.getUserbyId(message.getFromId()));
                messages.add(vo);
            }
            model.addAttribute("messages", messages);
        } catch (Exception e) {
            logger.error("获取详情失败" + e.getMessage());
        }
        return "letterDetail";
    }

    @RequestMapping(path = "/msg/addMessage", method = RequestMethod.POST)
    @ResponseBody //弹窗形式
    public String addMessage(@RequestParam("toName") String toName,
                             @RequestParam("content") String content) {

        try {
            if (hostHolder.getUser() == null) {
                return WendaUtil.getJSONString(999, "未登录");
            }
            User toUser = userService.getUserbyName(toName);
            if (toUser == null) {
                return WendaUtil.getJSONString(1, "用户不存在");
            }
            Message message = new Message();
            message.setFromId(hostHolder.getUser().getUserId());
            message.setToId(toUser.getUserId());
            message.setContent(content);
            message.setCreatedDate(new Date());
            messageService.addMessage(message); //为什么没设置conversationId？？
            return WendaUtil.getJSONString(0);
        } catch (Exception e) {
            logger.error("发送信息失败" + e.getMessage());
            return WendaUtil.getJSONString(1, "发信失败");
        }
    }
}
