package com.project.service;

import com.project.dao.UserDao;
import com.project.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Claire on 11/26/17.
 */
@Service
public class UserService {

    @Autowired
    UserDao userDao;

    public User getUser(int userId){
        return userDao.selectById(userId);
    }
}
