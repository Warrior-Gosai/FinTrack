package com.fintrack.controller;

import com.fintrack.model.Contact;
import com.fintrack.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
public class ContactController {

    @Autowired
    private ContactService contactService;

    // Show Contact Page
    @GetMapping("/contact")
    public String showContactForm(Model model) {
        model.addAttribute("contact", new Contact());
        return "contact_us"; // contact.html in templates
    }

    // Handle Form Submission
    @PostMapping("/contact")
    public String submitContactForm(@ModelAttribute Contact contact, Model model) {
        contactService.saveContact(contact);
        model.addAttribute("message", "Your message has been sent!");
        model.addAttribute("contact", new Contact()); // Clear the form
        return "contact_us";
    }
}
