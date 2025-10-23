package com.fintrack.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index"; // home.html
    }

    @GetMapping("/about")
    public String about() {
        return "about_us"; // about_us.html
    }

    // @GetMapping("/contact")
    // public String contact() {
    // return "contact_us"; // contact_us.html
    // }

    // @GetMapping("/signin")
    // public String signIn() {
    // return "signin"; // signin.html
    // }

    // @GetMapping("/signup")
    // public String signUp() {
    // return "signup"; // signup.html
    // }
}
