package com.endorodrigo.eComerce.controller;

import com.endorodrigo.eComerce.model.User;
import com.endorodrigo.eComerce.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AuthController {

    public static Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String login(@ModelAttribute("formUser") User user){
        logger.info("In login"+ user);
        String res = authService.loginUser(user.getEmail(),  user.getPassword());
        if(res != "Incorrect Password"){
            return "index";
        }else {
            return "login";
        }

    }

    @RequestMapping(value = "/login/register", method = RequestMethod.POST)
    public String register(@ModelAttribute("formRegister") User user){
        logger.info("Register User"+ user.getPassword());
        authService.registerUser(user);
        return "index";
    }
}
