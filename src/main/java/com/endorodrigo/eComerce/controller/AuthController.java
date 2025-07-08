package com.endorodrigo.eComerce.controller;

import com.endorodrigo.eComerce.model.User;
import com.endorodrigo.eComerce.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
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


    @GetMapping("/")
    public String index(ModelMap model) {
        logger.info("Iniciando loggin");
        return "login";
    }


    @GetMapping("/register")
    public String register(ModelMap model) {
        logger.info("Iniciando register");
        return "register";
    }

    @RequestMapping(value = "/app/index", method = RequestMethod.POST)
    public String login(@ModelAttribute("formUser") User user){
        logger.info("In login"+ user);
        String res = authService.loginUser(user.getEmail(),  user.getPassword());
        return "redirect:/app/index";

    }

    @RequestMapping(value = "/app/register", method = RequestMethod.POST)
    public String register(@ModelAttribute("formRegister") User user){
        logger.info("Register User"+ user.getPassword());
        authService.registerUser(user);
        return "index";
    }
}
