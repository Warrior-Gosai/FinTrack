// package com.fintrack.controller;

// import com.fintrack.model.User;
// import com.fintrack.service.UserService;
// import jakarta.servlet.http.HttpSession;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.*;

// import java.util.Random;

// @Controller
// public class ForgotPasswordController {

//     @Autowired
//     private UserService userService;

//     // Step 1: Enter email → generate OTP
//     @GetMapping("/forgot-password")
//     public String showForgotPasswordPage() {
//         return "forgot_password";
//     }

//     @PostMapping("/forgot-password")
//     public String generateOtp(@RequestParam String email, Model model, HttpSession session) {
//         User user = userService.findByEmail(email);

//         if (user == null) {
//             model.addAttribute("error", "❌ No user found with that email.");
//             return "forgot_password";
//         }

//         // Generate 6-digit OTP
//         int otp = new Random().nextInt(900000) + 100000;

//         // Store OTP and user in session
//         session.setAttribute("otp", otp);
//         session.setAttribute("userEmail", email);

//         model.addAttribute("generatedOtp", otp);
//         model.addAttribute("email", email);

//         return "verify_otp"; // new page for OTP verification
//     }

//     // Step 2: Verify OTP → show reset password form
//     @PostMapping("/verify-otp")
//     public String verifyOtp(@RequestParam int enteredOtp, Model model, HttpSession session) {
//         Integer otp = (Integer) session.getAttribute("otp");
//         String email = (String) session.getAttribute("userEmail");

//         if (otp == null || email == null) {
//             model.addAttribute("error", "Session expired! Try again.");
//             return "forgot_password";
//         }

//         if (otp.equals(enteredOtp)) {
//             // OTP matched
//             model.addAttribute("email", email);
//             return "reset_password"; // new page for password reset
//         } else {
//             model.addAttribute("error", "❌ Invalid OTP! Please try again.");
//             model.addAttribute("generatedOtp", otp); // still show OTP again for retry
//             return "verify_otp";
//         }
//     }

//     // Step 3: Reset password
//     @PostMapping("/reset-password")
//     public void resetPassword(@RequestParam String email,
//             @RequestParam String newPassword,
//             Model model,
//             HttpSession session) {

//         User user = userService.findByEmail(email);

//         if (user == null) {
//             model.addAttribute("error", "User not found!");
//             // return "reset_password";
//         }

//         user.setPassword(newPassword);
//         userService.saveUser(user);

//         session.removeAttribute("otp");
//         session.removeAttribute("userEmail");

//         model.addAttribute("message", "✅ Password changed successfully! Please log in.");
//         // return "signin";
//     }

//     @GetMapping("/reset-password")
//     public String showResetPasswordPage(Model model) {
//         model.addAttribute("email", "");
//         model.addAttribute("otp", "");
//         model.addAttribute("newPassword", "");
//         return "reset_password"; // reset-password.html
//     }
// }

package com.fintrack.controller;

import com.fintrack.model.User;
import com.fintrack.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@Controller
public class ForgotPasswordController {

    @Autowired
    private UserService userService;

    // Step 1: Show Forgot Password page
    @GetMapping("/forgot-password")
    public String showForgotPasswordPage() {
        return "forgot_password";
    }

    // Step 2: Generate OTP and display on screen
    @PostMapping("/forgot-password")
    public String generateOtp(@RequestParam String email, Model model, HttpSession session) {
        User user = userService.findByEmail(email);

        if (user == null) {
            model.addAttribute("error", "❌ No user found with that email.");
            return "forgot_password";
        }

        // Generate 6-digit OTP
        int otp = new Random().nextInt(900000) + 100000;

        // Save OTP and email in session
        session.setAttribute("otp", otp);
        session.setAttribute("userEmail", email);

        // Display OTP on screen
        model.addAttribute("generatedOtp", otp);
        model.addAttribute("email", email);

        return "verify_otp"; // OTP verification page
    }

    // Step 3: Verify OTP
    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam int enteredOtp, Model model, HttpSession session) {
        Integer otp = (Integer) session.getAttribute("otp");
        String email = (String) session.getAttribute("userEmail");

        if (otp == null || email == null) {
            model.addAttribute("error", "Session expired! Try again.");
            return "forgot_password";
        }

        if (otp.equals(enteredOtp)) {
            model.addAttribute("email", email);
            return "reset_password";
        } else {
            model.addAttribute("error", "❌ Invalid OTP! Try again.");
            model.addAttribute("generatedOtp", otp); // show OTP again
            model.addAttribute("email", email);
            return "verify_otp";
        }
    }

    // Step 4: Reset password
    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String email,
            @RequestParam String newPassword,
            Model model,
            HttpSession session) {

        User user = userService.findByEmail(email);

        if (user == null) {
            model.addAttribute("error", "User not found!");
            return "reset_password";
        }

        user.setPassword(newPassword);
        userService.saveUser(user);

        // clear session
        session.removeAttribute("otp");
        session.removeAttribute("userEmail");

        model.addAttribute("message", "✅ Password changed successfully! Please log in.");
        return "reset_password";
    }

    // Optional GET mapping (direct access to reset page)
    @GetMapping("/reset-password")
    public String showResetPasswordPage(Model model) {
        model.addAttribute("email", "");
        return "reset_password";
    }
}
