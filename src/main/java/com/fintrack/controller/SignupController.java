package com.fintrack.controller;

import com.fintrack.model.User;
import com.fintrack.service.UserService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class SignupController {

    @Autowired
    private UserService userService;

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String processSignup(@ModelAttribute("user") User user, Model model, HttpSession session) {
        // Check if user already exists
        if (userService.isUserAlreadyRegistered(user.getEmail())) {
            model.addAttribute("error", "User already exists with this email!");
            return "signup"; // Reload signup form
        }

        // Save new user
        userService.saveUser(user);

        // Store user in session immediately after signup
        session.setAttribute("loggedInUser", user);

        // Redirect to dashboard
        return "redirect:/dashboard";
    }

}
