package com.project.service;

import com.project.aspect.LogAspect;
import com.project.dao.LoginTicketDao;
import com.project.dao.UserDao;
import com.project.model.LoginTicket;
import com.project.model.User;
import com.project.util.WendaUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Claire on 11/26/17.
 */
@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Autowired
    UserDao userDao;
    @Autowired
    LoginTicketDao loginTicketDao;

    public static String INIT_HEAD = "/images/head.jpg";

    int ticketValidTime = 3600 * 24 * 100;//ticket有效时间：100天（单位秒）

    public Map<String, String> register(String username, String password) {
        Map<String, String> map = new HashMap<String, String>();
        if (StringUtils.isBlank(username)) {
            map.put("msg", "用户名不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("msg", "密码不能为空");
            return map;
        }
        User user = userDao.selectByName(username);
        if (user != null) {
            map.put("msg", "用户名已经被注册");
            return map;
        }
        user = new User(username);
        String salt = UUID.randomUUID().toString().substring(0, 5);
        user.setSalt(salt);
        user.setPassword(getMD5(password, salt));
        user.setHeadUrl(INIT_HEAD);
        userDao.addUser(user);
        user = userDao.selectByName(user.getName());

        String ticket = addLoginTicket(user.getUserId());
        map.put("ticket", ticket);

        return map;
    }

    public Map<String, String> login(String username, String password) {
        Map<String, String> map = new HashMap<String, String>();
        if (StringUtils.isBlank(username)) {
            map.put("msg", "用户名不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("msg", "密码不能为空");
            return map;
        }
        User user = userDao.selectByName(username);
        if (user == null) {
            map.put("msg", "用户名不存在");
            return map;
        }

        if (!getMD5(password, user.getSalt()).equals(user.getPassword())) {
            map.put("msg", "密码错误");
            return map;
        }

        String ticket = addLoginTicket(user.getUserId());
        map.put("ticket", ticket);
        return map;
    }

    public String addLoginTicket(int userId) {
        LoginTicket loginTicket = new LoginTicket(userId, 0);
        Date expireTime = new Date();
        expireTime.setTime(ticketValidTime + expireTime.getTime());
        loginTicket.setExpired(expireTime);
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
        loginTicketDao.addTicket(loginTicket);
        return loginTicket.getTicket();
    }

    public void logout(String ticket) {
        loginTicketDao.updateStatus(ticket, 1);
    }

    public User getUser(int userId) {
        return userDao.selectById(userId);
    }

    private String getMD5(String psd, String salt) {
        String base = psd + "/" + salt;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }
}
