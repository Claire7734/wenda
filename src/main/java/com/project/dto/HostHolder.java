package com.project.dto;

import com.project.model.User;
import org.springframework.stereotype.Component;

/**
 * Created by Claire on 11/29/17.
 */
@Component
public class HostHolder {

    //为每一个线程分配一个user
    private static ThreadLocal<User> users = new ThreadLocal<User>();

    public User getUser(){
        return users.get();
    }

    public void setUsers(User user){
        users.set(user);
    }

    public void clear(){
        users.remove();
    }
}
