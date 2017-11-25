package com.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Claire on 11/25/17.
 */
@Controller
public class IndexController {

    @RequestMapping("/")
    public String index(){
        return "home";
    }

    @RequestMapping("/profile/{userId}")
    @ResponseBody
    public String profile(@PathVariable int userId){
        return String.format("profile page of %d",userId);
    }

}
