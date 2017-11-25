package com.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Claire on 11/25/17.
 */
@Controller
public class IndexController {

    @GetMapping("/")
    @ResponseBody
    public String index(){
        return "hello";
    }
}
