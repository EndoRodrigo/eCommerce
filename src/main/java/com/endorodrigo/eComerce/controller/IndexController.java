package com.endorodrigo.eComerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class IndexController {

    @GetMapping("/")
    public String index(ModelMap model) {
        return "login";
    }

    @GetMapping("/login")
    public String login(ModelMap model) {
        return "index";
    }

    @GetMapping("/register")
    public String register(ModelMap model) {
        return "register";
    }


}
