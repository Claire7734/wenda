package com.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Claire on 11/25/17.
 */
//@Controller
public class IndexController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String home(Model model) {
        model.addAttribute("value1", 1);
        List<String> colors = Arrays.asList(new String[]{"red", "blue", "green"});
        model.addAttribute("colors", colors);

        return "home";
    }

    @RequestMapping("/profile/{userId}")
    @ResponseBody
    public String profile(@PathVariable int userId) {
        return String.format("profile page of %d", userId);
    }

}
