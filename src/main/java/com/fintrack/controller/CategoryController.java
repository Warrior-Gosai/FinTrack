package com.fintrack.controller;

import com.fintrack.model.Category;
import com.fintrack.model.User;
import com.fintrack.service.CategoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // Display all categories for logged in user
    @GetMapping
    public String listCategories(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser"); // use correct session key
        if (user == null)
            return "redirect:/signin";

        model.addAttribute("categories", categoryService.getAllCategoriesByUser(user.getId()));
        return "category"; // your HTML file is category.html
    }

    // Add category
    @PostMapping("/add")
    public String addCategory(@ModelAttribute Category category, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null)
            return "redirect:/signin";

        category.setUser(user);
        categoryService.saveCategory(category);

        return "redirect:/categories"; // redirect so refresh doesn’t resubmit
    }

    // Delete category
    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null)
            return "redirect:/signin";

        categoryService.deleteCategory(id);
        return "redirect:/categories";
    }
}
