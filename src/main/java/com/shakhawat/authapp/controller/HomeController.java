package com.shakhawat.authapp.controller;

import com.shakhawat.authapp.service.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String dashboard(Model model) {

        model.addAttribute("message", "Hi %s! Welcome to the %s Dashboard!".formatted(SecurityUtils.getCurrentUserFullName(), SecurityUtils.getCurrentUserRole()));

        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

}
