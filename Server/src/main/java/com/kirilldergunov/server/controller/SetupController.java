package com.kirilldergunov.server.controller;

import com.kirilldergunov.server.dto.CreateAdminRequest;
import com.kirilldergunov.server.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/setup")
public class SetupController {

    private final UserService userService;

    public SetupController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/setup")
    public String setupPage(Model model) {

        if (userService.hasAnyUsers()) {
            return "redirect:/login";
        }

        model.addAttribute("admin", new CreateAdminRequest());
        return "setup";
    }

    @PostMapping
    public String createAdmin(@ModelAttribute("admin") CreateAdminRequest request) {

        userService.createAdmin(request.getUsername(), request.getPassword());
        return "redirect:/login";
    }
}