package com.fintrack.controller;

import com.fintrack.model.User;
import com.fintrack.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class SigninController {

    @Autowired
    private UserService userService;

    @GetMapping("/signin")
    public String showSignin(Model model, @RequestParam(value = "error", required = false) String err) {
        model.addAttribute("user", new User());
        if (err != null)
            model.addAttribute("error", err.toString());
        return "signin";
    }

    @PostMapping("/signin")
    public String loginUser(@ModelAttribute("user") User userForm, HttpSession session, Model model) {

        // Lookup user by email
        User user = userService.findByEmail(userForm.getEmail());

        if (user != null && user.getPassword().equals(userForm.getPassword())) {
            // Valid credentials, store in session
            session.setAttribute("loggedUser", user);

            // Redirect to dashboard
            return "redirect:/dashboard";
        } else {
            // Invalid credentials, return to login with error
            String errorMsg = "Invalid email or password for email: " + userForm.getEmail();

            // Add error to model to show in UI
            model.addAttribute("error", errorMsg);

            return "signin";
        }
    }
}