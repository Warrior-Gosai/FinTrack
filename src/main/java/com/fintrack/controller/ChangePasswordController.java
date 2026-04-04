package com.fintrack.controller;

import com.fintrack.model.User;
import com.fintrack.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ChangePasswordController {

    @Autowired
    private UserService userService;

    // Show the change password page
    @GetMapping("/change-password")
    public String showChangePasswordPage(HttpSession session, Model model) {
        User loggedUser = (User) session.getAttribute("loggedUser");

        if (loggedUser == null) {
            return "redirect:/signin";
        }

        model.addAttribute("email", loggedUser.getEmail());
        return "change_password";
    }

    // Handle password change request
    @PostMapping("/change-password")
    public String changePassword(@RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            HttpSession session,
            Model model) {

        User loggedUser = (User) session.getAttribute("loggedUser");

        if (loggedUser == null) {
            return "redirect:/signin";
        }

        if (!loggedUser.getPassword().equals(currentPassword)) {
            model.addAttribute("error", "❌ Current password is incorrect.");
            return "change_password";
        }

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "❌ New passwords do not match.");
            return "change_password";
        }

        // Update the password
        loggedUser.setPassword(newPassword);
        userService.saveUser(loggedUser);

        model.addAttribute("message", "✅ Password updated successfully!");
        return "change_password";
    }
}
